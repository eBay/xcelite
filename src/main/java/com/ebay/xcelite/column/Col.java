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

/**
 * Represents a Column object which holds all data about the Excel column.
 *
 * @author kharel (kharel@ebay.com)
 * created Aug 29, 2013
 */
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

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getName() {
        return name;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public Class<? extends ColumnValueConverter<?, ?>> getConverter() {
        return converter;
    }

    public void setConverter(Class<? extends ColumnValueConverter<?, ?>> converter) {
        this.converter = converter;
    }

    public boolean isAnyColumn() {
        return isAnyColumn;
    }

    public void setAnyColumn(boolean isAnyColumn) {
        this.isAnyColumn = isAnyColumn;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Col other = (Col) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public int compareTo(Col o) {
        return this.getName().compareTo(o.getName());
    }
}
