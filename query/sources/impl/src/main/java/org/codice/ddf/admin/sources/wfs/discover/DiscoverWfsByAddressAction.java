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
 */
package org.codice.ddf.admin.sources.wfs.discover;

import static org.codice.ddf.admin.sources.commons.SourceActionCommons.createSourceInfoField;

import java.util.List;

import org.codice.ddf.admin.api.fields.Field;
import org.codice.ddf.admin.common.Result;
import org.codice.ddf.admin.common.actions.BaseAction;
import org.codice.ddf.admin.common.fields.common.AddressField;
import org.codice.ddf.admin.common.fields.common.CredentialsField;
import org.codice.ddf.admin.common.fields.common.UrlField;
import org.codice.ddf.admin.sources.commons.utils.WfsSourceUtils;
import org.codice.ddf.admin.sources.fields.SourceInfoField;
import org.codice.ddf.admin.sources.fields.type.SourceConfigUnionField;

import com.google.common.collect.ImmutableList;

public class DiscoverWfsByAddressAction extends BaseAction<SourceInfoField> {

    public static final String ID = "discoverWfsByAddress";

    public static final String DESCRIPTION =
            "Attempts to discover WFS sources with the given hostname and port, and optionally a username and password if authentication is enabled.";

    private AddressField addressField;

    private CredentialsField credentialsField;

    private WfsSourceUtils wfsSourceUtils;

    public DiscoverWfsByAddressAction() {
        super(ID, DESCRIPTION, new SourceInfoField());
        wfsSourceUtils = new WfsSourceUtils();
        addressField = new AddressField();
        credentialsField = new CredentialsField();
        addressField.useDefaultRequired();
        credentialsField.allFieldsRequired(false);
    }

    public DiscoverWfsByAddressAction(WfsSourceUtils wfsSourceUtils) {
        this();
        this.wfsSourceUtils = wfsSourceUtils;
    }

    @Override
    public SourceInfoField performAction() {
        Result<UrlField> discoveredUrl = wfsSourceUtils.discoverWfsUrl(addressField, credentialsField);
        addArgumentMessages(discoveredUrl.argumentMessages());
        if(containsErrorMsgs()) {
            return null;
        }

        Result<SourceConfigUnionField> configResult = wfsSourceUtils.getPreferredWfsConfig(discoveredUrl.get(), credentialsField);
        addArgumentMessages(configResult.argumentMessages());
        if(containsErrorMsgs()) {
            return null;
        }

        return createSourceInfoField(ID, true, configResult.get());
    }

    @Override
    public List<Field> getArguments() {
        return ImmutableList.of(addressField, credentialsField);
    }
}