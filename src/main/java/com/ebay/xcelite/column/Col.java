/*
  Copyright [2013-2014] eBay Software Foundation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.ebay.xcelite.column;

import com.ebay.xcelite.converters.ColumnValueConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a column object which holds all data about the Excel column.
 *
 * @author kharel (kharel@ebay.com)
 * @since 1.0
 * created Aug 29, 2013
 */
@Data
@EqualsAndHashCode(of="name")
public class Col implements Comparable<Col> {

    private final String name;
    private String fieldName;
    private Class<?> type;
    private String dataFormat;
    private Class<? extends ColumnValueConverter<?, ?>> converter;
    private boolean isAnyColumn = false;

    public Col(String name) {
        this(name, null);
    }

    public Col(String name, String fieldName) {
        this.name = name;
        this.fieldName = fieldName;
        type = String.class;
    }

    @Override
    public String toString() {
        return "name=" + name + ",fieldName=" + fieldName + ",type=" + type + ",converter=" + converter;
    }

    public void copyTo(Col col) {
        col.fieldName = fieldName;
        col.type = type;
        col.dataFormat = dataFormat;
        col.converter = converter;
    }


    @Override
    public int compareTo(Col col) {
        return this.getName().compareTo(col.getName());
    }
}
