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
package org.codice.ddf.admin.beta.types;

import org.codice.ddf.admin.common.fields.base.scalar.StringField;

public class XmlField extends StringField {

    public static final String FIELD_NAME = "xml";

    public static final String FIELD_TYPE_NAME = "Xml";

    public static final String DESCRIPTION = "An XML encoded string.";

    public XmlField() {
        this(FIELD_NAME);
    }

    public XmlField(String fieldName) {
        super(fieldName, FIELD_TYPE_NAME, DESCRIPTION);
    }

}
