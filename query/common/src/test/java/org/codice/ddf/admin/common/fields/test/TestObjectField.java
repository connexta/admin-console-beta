/**
 * Copyright (c) Codice Foundation
 *
 * <p>This is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public
 * License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ddf.admin.common.fields.test;

import static org.codice.ddf.admin.common.fields.test.TestFieldProvider.LIST_FIELD_NAME;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import org.codice.ddf.admin.api.Field;
import org.codice.ddf.admin.api.fields.ObjectField;
import org.codice.ddf.admin.common.fields.base.BaseListField;
import org.codice.ddf.admin.common.fields.base.BaseObjectField;
import org.codice.ddf.admin.common.fields.base.scalar.BooleanField;
import org.codice.ddf.admin.common.fields.base.scalar.IntegerField;
import org.codice.ddf.admin.common.fields.base.scalar.StringField;

public class TestObjectField extends BaseObjectField {

  public static final String FIELD_NAME = "testObj";

  public static final Integer SAMPLE_INTEGER_VALUE = 8675309;

  public static final boolean SAMPLE_BOOLEAN_VALUE = true;

  public static final String SAMPLE_STRING_VALUE = "SAMPLE_STRING";

  public static final List<String> SAMPLE_LIST_VALUE = Arrays.asList("entry1", "entry2", "entry3");

  public static final String SAMPLE_ENUM = TestEnumField.EnumA.ENUM_A;

  public static final String SUB_FIELD_OF_INNER_FIELD_NAME = "testSubField";

  public static final String HIDDEN_FIELD_NAME = "testHidden";

  public static final String SAMPLE_HIDDEN_FIELD_VALUE = "SAMPLE_HIDDEN_FIELD";

  public static final String INNER_OBJECT_FIELD_NAME = "innerObjectField";

  public static final String OBJECT_FIELD_TEST_ERROR = "OBJECT_FIELD_TEST_ERROR";

  private InnerTestObjectField innerTestObjectField;

  private IntegerField integerField;

  private BooleanField booleanField;

  private StringField stringField;

  private StringField.ListImpl listField;

  private TestEnumField enumField;

  private TestHiddenField hiddenField;

  public TestObjectField() {
    super(FIELD_NAME, "TestObjectField", "A sample object containing all supported base types.");
    integerField = new IntegerField();
    booleanField = new BooleanField();
    stringField = new StringField();
    listField = new StringField.ListImpl(LIST_FIELD_NAME).useDefaultRequired();
    enumField = new TestEnumField();
    innerTestObjectField = new InnerTestObjectField();
    hiddenField = new TestHiddenField();
  }

  public IntegerField getIntegerField() {
    return integerField;
  }

  public BooleanField getBooleanField() {
    return booleanField;
  }

  public StringField getStringField() {
    return stringField;
  }

  public StringField.ListImpl getListField() {
    return listField;
  }

  public TestEnumField getEnumField() {
    return enumField;
  }

  public ObjectField getInnerObjectField() {
    return innerTestObjectField;
  }

  public TestHiddenField getHiddenField() {
    return hiddenField;
  }

  public TestObjectField setInteger(Integer val) {
    this.integerField.setValue(val);
    return this;
  }

  public TestObjectField setBoolean(boolean val) {
    booleanField.setValue(val);
    return this;
  }

  public TestObjectField setString(String val) {
    stringField.setValue(val);
    return this;
  }

  public TestObjectField setList(List<String> strs) {
    listField.setValue(strs);
    return this;
  }

  public TestObjectField setEnum(String val) {
    enumField.setValue(val);
    return this;
  }

  public TestObjectField setInnerObject(Map<String, Object> values) {
    innerTestObjectField.setValue(values);
    return this;
  }

  public TestObjectField setHiddenField(String value) {
    hiddenField.setValue(value);
    return this;
  }

  public static TestObjectField createSampleTestObject() {
    return new TestObjectField()
        .setInteger(SAMPLE_INTEGER_VALUE)
        .setBoolean(SAMPLE_BOOLEAN_VALUE)
        .setString(SAMPLE_STRING_VALUE)
        .setList(SAMPLE_LIST_VALUE)
        .setEnum(SAMPLE_ENUM)
        .setInnerObject(
            ImmutableMap.of(
                SUB_FIELD_OF_INNER_FIELD_NAME,
                InnerTestObjectField.TEST_VALUE,
                HIDDEN_FIELD_NAME,
                InnerTestObjectField.TEST_HIDDEN_FIELD_VALUE))
        .setHiddenField(SAMPLE_HIDDEN_FIELD_VALUE);
  }

  @Override
  public List<Field> getFields() {
    return ImmutableList.of(
        integerField,
        booleanField,
        stringField,
        listField,
        enumField,
        innerTestObjectField,
        hiddenField);
  }

  @Override
  public Set<String> getErrorCodes() {
    return new ImmutableSet.Builder<String>()
        .addAll(super.getErrorCodes())
        .add(OBJECT_FIELD_TEST_ERROR)
        .build();
  }

  public class InnerTestObjectField extends BaseObjectField {

    public static final String FIELD_TYPE_NAME = "InnerTestObjectField";

    public static final String DESCRIPTION = "InnerTestObjectField Description";

    public static final String TEST_VALUE = "testValue";

    public static final String TEST_HIDDEN_FIELD_VALUE = "testValue";

    public static final String INNER_OBJECT_FIELD_TEST_ERROR = "INNER_OBJECT_FIELD_TEST_ERROR";

    public StringField subFieldOfInnerField;

    public TestHiddenField subHiddenField;

    InnerTestObjectField() {
      this(INNER_OBJECT_FIELD_NAME, FIELD_TYPE_NAME, DESCRIPTION);
    }

    InnerTestObjectField(String fieldName, String fieldTypeName, String description) {
      super(fieldName, fieldTypeName, description);
      subFieldOfInnerField = new StringField(SUB_FIELD_OF_INNER_FIELD_NAME);
      subHiddenField = new TestHiddenField();
    }

    public InnerTestObjectField setSubField(String value) {
      subFieldOfInnerField.setValue(value);
      return this;
    }

    public InnerTestObjectField setHiddenField(String value) {
      subHiddenField.setValue(value);
      return this;
    }

    @Override
    public List<Field> getFields() {
      return ImmutableList.of(subFieldOfInnerField, subHiddenField);
    }

    @Override
    public Set<String> getErrorCodes() {
      return new ImmutableSet.Builder<String>()
          .addAll(super.getErrorCodes())
          .add(INNER_OBJECT_FIELD_TEST_ERROR)
          .build();
    }
  }

  public static class ListImpl extends BaseListField<TestObjectField> {

    public ListImpl(String fieldName) {
      super(fieldName);
    }

    public ListImpl() {
      this(LIST_FIELD_NAME);
    }

    @Override
    public Callable<TestObjectField> getCreateListEntryCallable() {
      return TestObjectField::new;
    }
  }
}
