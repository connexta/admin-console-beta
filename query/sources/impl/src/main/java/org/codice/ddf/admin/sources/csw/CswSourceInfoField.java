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
package org.codice.ddf.admin.sources.csw;

import java.util.List;

import org.codice.ddf.admin.api.Field;
import org.codice.ddf.admin.sources.fields.SourceInfoField;
import org.codice.ddf.admin.sources.fields.type.CswSourceConfigurationField;

import com.google.common.collect.ImmutableList;

public class CswSourceInfoField extends SourceInfoField {

    public static final String DEFAULT_FIELD_NAME = "cswSourceInfo";

    public static final String FIELD_TYPE_NAME = "CswSourceInfo";

    public static final String DESCRIPTION =
            "Contains the availability and properties of the CSW source.";

    private CswSourceConfigurationField config;

    public CswSourceInfoField() {
        super(DEFAULT_FIELD_NAME, FIELD_TYPE_NAME, DESCRIPTION);
        config = new CswSourceConfigurationField();
        updateInnerFieldPaths();
    }

    public CswSourceInfoField config(CswSourceConfigurationField config) {
        this.config = config;
        return this;
    }

    public CswSourceConfigurationField config() {
        return config;
    }

    @Override
    public List<Field> getFields() {
        return new ImmutableList.Builder<Field>().addAll(super.getFields())
                .add(config)
                .build();
    }
}
