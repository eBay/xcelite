/*
 * Copyright 2018 Thanthathon.b.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ebay.xcelite.model;

import com.ebay.xcelite.converters.ColumnValueConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Thanthathon.b
 */
public class ThaiStringCellDateConverter implements ColumnValueConverter<String, Date> {
    static final String DATE_PATTERN = "dd/MM/yyyy";

    @Override
    public String serialize(Date value) {
        DateFormat df = new SimpleDateFormat(DATE_PATTERN, new Locale("th", "TH"));
        return df.format(value);
    }

    @Override
    public Date deserialize(String value) {
        DateFormat df = new SimpleDateFormat(DATE_PATTERN, new Locale("th", "TH"));
        try {
            return df.parse(value);
        } catch (ParseException ex) {
            return null;
        }
    }

}
