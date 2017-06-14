/**
 * Copyright (c) Codice Foundation
 * <p>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 **/
package org.codice.ddf.admin.ldap.discover

import org.codice.ddf.admin.api.fields.FunctionField
import org.codice.ddf.admin.api.report.FunctionReport
import org.codice.ddf.admin.common.fields.common.CredentialsField
import org.codice.ddf.admin.common.fields.common.HostnameField
import org.codice.ddf.admin.common.fields.common.PortField
import org.codice.ddf.admin.common.report.message.DefaultMessages
import org.codice.ddf.admin.ldap.LdapTestingCommons
import org.codice.ddf.admin.ldap.TestLdapServer
import org.codice.ddf.admin.ldap.commons.LdapMessages
import org.codice.ddf.admin.ldap.fields.config.LdapSettingsField
import org.codice.ddf.admin.ldap.fields.config.LdapUseCase
import org.codice.ddf.admin.ldap.fields.connection.LdapBindMethod
import org.codice.ddf.admin.ldap.fields.connection.LdapBindUserInfo
import org.codice.ddf.admin.ldap.fields.connection.LdapConnectionField
import org.codice.ddf.admin.ldap.fields.connection.LdapEncryptionMethodField
import spock.lang.Specification

import static org.codice.ddf.admin.ldap.fields.config.LdapUseCase.ATTRIBUTE_STORE
import static org.codice.ddf.admin.ldap.fields.config.LdapUseCase.AUTHENTICATION

class TestLdapSettingsTest extends Specification {
    static TestLdapServer server
    Map<String, Object> args
    LdapTestSettings action
    def badPaths
    def baseMsg

    def setupSpec() {
        server = TestLdapServer.getInstance().useSimpleAuth()
        server.startListening()
    }

    def cleanupSpec() {
        server.shutdown()
        server = null
    }

    def setup() {
        LdapTestingCommons.loadLdapTestProperties()
        action = new LdapTestSettings()

        // Initialize bad paths
        baseMsg = [LdapTestSettings.ID, FunctionField.ARGUMENT]
        badPaths = [missingHostPath         : baseMsg + [LdapConnectionField.DEFAULT_FIELD_NAME, HostnameField.DEFAULT_FIELD_NAME],
                    missingPortPath         : baseMsg + [LdapConnectionField.DEFAULT_FIELD_NAME, PortField.DEFAULT_FIELD_NAME],
                    missingEncryptPath      : baseMsg + [LdapConnectionField.DEFAULT_FIELD_NAME, LdapEncryptionMethodField.DEFAULT_FIELD_NAME],
                    missingUsernamePath     : baseMsg + [LdapBindUserInfo.DEFAULT_FIELD_NAME, CredentialsField.DEFAULT_FIELD_NAME, CredentialsField.USERNAME_FIELD_NAME],
                    missingUserpasswordPath : baseMsg + [LdapBindUserInfo.DEFAULT_FIELD_NAME, CredentialsField.DEFAULT_FIELD_NAME, CredentialsField.PASSWORD_FIELD_NAME],
                    missingBindMethodPath   : baseMsg + [LdapBindUserInfo.DEFAULT_FIELD_NAME, LdapBindMethod.DEFAULT_FIELD_NAME],
                    missingUseCasePath      : baseMsg + [LdapSettingsField.DEFAULT_FIELD_NAME, LdapUseCase.DEFAULT_FIELD_NAME],
                    missingUserPath         : baseMsg + [LdapSettingsField.DEFAULT_FIELD_NAME, 'baseUserDn'],
                    missingGroupPath        : baseMsg + [LdapSettingsField.DEFAULT_FIELD_NAME, 'baseGroupDn'],
                    missingUserNameAttrPath : baseMsg + [LdapSettingsField.DEFAULT_FIELD_NAME, 'userNameAttribute'],
                    missingGroupObjectPath  : baseMsg + [LdapSettingsField.DEFAULT_FIELD_NAME, 'groupObjectClass'],
                    missingGroupAttribPath  : baseMsg + [LdapSettingsField.DEFAULT_FIELD_NAME, 'groupAttributeHoldingMember'],
                    missingMemberAttribPath : baseMsg + [LdapSettingsField.DEFAULT_FIELD_NAME, 'memberAttributeReferencedInGroup'],
                    missingAttribMappingPath: baseMsg + [LdapSettingsField.DEFAULT_FIELD_NAME, 'attributeMapping'],
                    badUserDnPath           : baseMsg + [LdapSettingsField.DEFAULT_FIELD_NAME, 'baseUserDn'],
                    badGroupDnPath          : baseMsg + [LdapSettingsField.DEFAULT_FIELD_NAME, 'baseGroupDn']

        ]
    }

    def 'fail on missing required fields'() {
        setup:
        action = new LdapTestSettings()

        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): new LdapConnectionField().getValue()]
        action.setValue(args)

        when:
        FunctionReport report = action.getValue()

        then:
        report.messages().size() == 10
        report.messages().count {
            it.getCode() == DefaultMessages.MISSING_REQUIRED_FIELD
        } == 10

        report.messages()*.getPath() as Set == [badPaths.missingHostPath,
                                                badPaths.missingPortPath,
                                                badPaths.missingEncryptPath,
                                                badPaths.missingUsernamePath,
                                                badPaths.missingUserpasswordPath,
                                                badPaths.missingBindMethodPath,
                                                badPaths.missingUseCasePath,
                                                badPaths.missingUserPath,
                                                badPaths.missingGroupPath,
                                                badPaths.missingUserNameAttrPath] as Set
    }

    def 'fail on missing required fields for LDAP Attribute Store'() {
        setup:
        def ldapSettings = initLdapSettings(ATTRIBUTE_STORE)

        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): noEncryptionLdapConnectionInfo().getValue(),
                (LdapBindUserInfo.DEFAULT_FIELD_NAME)   : simpleBindInfo().getValue(),
                (LdapSettingsField.DEFAULT_FIELD_NAME)  : ldapSettings.getValue()]
        action.setValue(args)
        action.setTestingUtils(new TestLdapConnectionTest.LdapTestingUtilsMock())

        when:
        FunctionReport report = action.getValue()

        then:
        report.messages().size() == 4
        report.messages().count {
            it.getCode() == DefaultMessages.MISSING_REQUIRED_FIELD
        } == 4
        report.messages()*.getPath() as Set == [badPaths.missingGroupObjectPath,
                                                badPaths.missingGroupAttribPath,
                                                badPaths.missingMemberAttribPath,
                                                badPaths.missingAttribMappingPath] as Set
    }

    def 'fail with invalid user/group dn'() {
        setup:
        def ldapSettings = initLdapSettings(ATTRIBUTE_STORE, true)
                .baseUserDn('BAD')
                .baseGroupDn('BAD')

        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): noEncryptionLdapConnectionInfo().getValue(),
                (LdapBindUserInfo.DEFAULT_FIELD_NAME)   : simpleBindInfo().getValue(),
                (LdapSettingsField.DEFAULT_FIELD_NAME)  : ldapSettings.getValue()]
        action.setValue(args)
        action.setTestingUtils(new TestLdapConnectionTest.LdapTestingUtilsMock())

        when:
        FunctionReport report = action.getValue()

        then:
        report.messages().size() == 2
        report.messages().count {
            it.getCode() == 'INVALID_DN'
        } == 2

        report.messages()*.getPath() as Set == [badPaths.badUserDnPath,
                                                badPaths.badGroupDnPath] as Set
    }

    def 'fail to connect to LDAP'() {
        setup:
        def ldapSettings = initLdapSettings(AUTHENTICATION)

        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): noEncryptionLdapConnectionInfo().port(666).getValue(),
                (LdapBindUserInfo.DEFAULT_FIELD_NAME)   : simpleBindInfo().getValue(),
                (LdapSettingsField.DEFAULT_FIELD_NAME)  : ldapSettings.getValue()]
        action.setValue(args)
        action.setTestingUtils(new TestLdapConnectionTest.LdapTestingUtilsMock())

        when:
        FunctionReport report = action.getValue()

        then:
        report.messages().size() == 1
        !report.result().getValue()
        report.messages().get(0).getCode() == DefaultMessages.CANNOT_CONNECT
        report.messages().get(0).getPath() == baseMsg + [LdapConnectionField.DEFAULT_FIELD_NAME]
    }

    def 'fail to bind to LDAP'() {
        setup:
        def ldapSettings = initLdapSettings(AUTHENTICATION)

        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): noEncryptionLdapConnectionInfo().getValue(),
                (LdapBindUserInfo.DEFAULT_FIELD_NAME)   : simpleBindInfo().password('badPassword').getValue(),
                (LdapSettingsField.DEFAULT_FIELD_NAME)  : ldapSettings.getValue()]
        action.setValue(args)
        action.setTestingUtils(new TestLdapConnectionTest.LdapTestingUtilsMock())

        when:
        FunctionReport report = action.getValue()

        then:
        report.messages().size() == 1
        !report.result().getValue()
        report.messages().get(0).getCode() == LdapMessages.CANNOT_BIND
        report.messages().get(0).getPath() == baseMsg + [LdapBindUserInfo.DEFAULT_FIELD_NAME]
    }

    def 'fail when baseUserDN & baseGroupDN do not exist'() {
        setup:
        def ldapSettings = initLdapSettings(ATTRIBUTE_STORE, true)
                .baseUserDn('ou=users,dc=example,dc=BAD')
                .baseGroupDn('ou=groups,dc=example,dc=BAD')

        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): noEncryptionLdapConnectionInfo().getValue(),
                (LdapBindUserInfo.DEFAULT_FIELD_NAME)   : simpleBindInfo().getValue(),
                (LdapSettingsField.DEFAULT_FIELD_NAME)  : ldapSettings.getValue()]
        action.setValue(args)
        action.setTestingUtils(new TestLdapConnectionTest.LdapTestingUtilsMock())

        when:
        FunctionReport report = action.getValue()

        then:
        report.messages().size() == 2
        report.messages().count {
            it.getCode() == 'DN_DOES_NOT_EXIST'
        } == 2

        report.messages()*.getPath() as Set == [badPaths.badUserDnPath,
                                                badPaths.badGroupDnPath] as Set
    }

    def 'fail to find entries in baseGroupDn'() {
        setup:
        def ldapSettings = initLdapSettings(ATTRIBUTE_STORE, true)
                .baseGroupDn('ou=emptygroups,dc=example,dc=com')

        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): noEncryptionLdapConnectionInfo().getValue(),
                (LdapBindUserInfo.DEFAULT_FIELD_NAME)   : simpleBindInfo().getValue(),
                (LdapSettingsField.DEFAULT_FIELD_NAME)  : ldapSettings.getValue()]
        action.setValue(args)
        action.setTestingUtils(new TestLdapConnectionTest.LdapTestingUtilsMock())

        when:
        FunctionReport report = action.getValue()

        then:
        report.messages().size() == 2
        report.messages().count {
            it.getCode() == 'NO_GROUPS_IN_BASE_GROUP_DN'
        } == 2

        report.messages()*.getPath() as Set == [badPaths.badGroupDnPath,
                                                badPaths.missingGroupObjectPath] as Set
    }

    def 'fail to find specified groupObjectClass in groups'() {
        setup:
        def ldapSettings = initLdapSettings(ATTRIBUTE_STORE, true)
                .groupObjectClass('BADOBJECTCLASS')

        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): noEncryptionLdapConnectionInfo().getValue(),
                (LdapBindUserInfo.DEFAULT_FIELD_NAME)   : simpleBindInfo().getValue(),
                (LdapSettingsField.DEFAULT_FIELD_NAME)  : ldapSettings.getValue()]
        action.setValue(args)
        action.setTestingUtils(new TestLdapConnectionTest.LdapTestingUtilsMock())

        when:
        FunctionReport report = action.getValue()

        then:
        report.messages().size() == 2
        report.messages().count {
            it.getCode() == 'NO_GROUPS_IN_BASE_GROUP_DN'
        } == 2

        report.messages()*.getPath() as Set == [badPaths.badGroupDnPath,
                                                badPaths.missingGroupObjectPath] as Set
    }

    def 'fail to find entries in baseUserDN'() {
        setup:
        def ldapSettings = initLdapSettings(ATTRIBUTE_STORE, true)
                .baseUserDn('ou=emptyusers,dc=example,dc=com')

        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): noEncryptionLdapConnectionInfo().getValue(),
                (LdapBindUserInfo.DEFAULT_FIELD_NAME)   : simpleBindInfo().getValue(),
                (LdapSettingsField.DEFAULT_FIELD_NAME)  : ldapSettings.getValue()]
        action.setValue(args)
        action.setTestingUtils(new TestLdapConnectionTest.LdapTestingUtilsMock())

        when:
        FunctionReport report = action.getValue()

        then:
        report.messages().size() == 2
        report.messages().count {
            it.getCode() == 'NO_USERS_IN_BASE_USER_DN'
        } == 1
        report.messages().count {
            it.getCode() == 'USER_NAME_ATTRIBUTE_NOT_FOUND'
        } == 1

        report.messages()*.getPath() as Set == [badPaths.badUserDnPath,
                                                badPaths.missingUserNameAttrPath] as Set
    }

    def 'succeed'() {
        setup:
        def ldapSettings = initLdapSettings(ATTRIBUTE_STORE, true)

        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): noEncryptionLdapConnectionInfo().getValue(),
                (LdapBindUserInfo.DEFAULT_FIELD_NAME)   : simpleBindInfo().getValue(),
                (LdapSettingsField.DEFAULT_FIELD_NAME)  : ldapSettings.getValue()]
        action.setValue(args)
        action.setTestingUtils(new TestLdapConnectionTest.LdapTestingUtilsMock())

        when:
        FunctionReport report = action.getValue()

        then:
        report.messages().empty
        report.result().getValue()
    }

    LdapConnectionField noEncryptionLdapConnectionInfo() {
        return new LdapConnectionField()
                .hostname(server.getHostname())
                .port(server.getLdapPort())
                .encryptionMethod(LdapEncryptionMethodField.NONE)
    }

    LdapBindUserInfo simpleBindInfo() {
        return new LdapBindUserInfo().bindMethod(LdapBindMethod.SIMPLE).username(server.getBasicAuthDn()).password(server.getBasicAuthPassword())
    }

    private LdapSettingsField initLdapSettings(String useCase, boolean includeAttributeFields = false) {
        def settingsField = new LdapSettingsField()
                .usernameAttribute('sn')
                .baseUserDn('ou=users,dc=example,dc=com')
                .baseGroupDn('ou=groups,dc=example,dc=com')
                .useCase(useCase)
        if (includeAttributeFields) {
            settingsField.groupObjectClass('groupOfNames')
                    .groupAttributeHoldingMember('member')
                    .memberAttributeReferencedInGroup('uid')
                    .attributeMapField(['foobar': 'cn'])
        }

        settingsField
    }
}