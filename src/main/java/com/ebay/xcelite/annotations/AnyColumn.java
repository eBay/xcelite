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
package com.ebay.xcelite.annotations;

import com.ebay.xcelite.annotate.NoConverterClass;
import com.ebay.xcelite.converters.ColumnValueConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

/**
 * Annotation to annotate a {@link java.util.Map Map} field which represents K/V
 * as column and value in excel file.
 *
 * @author kharel (kharel@ebay.com)
 * created Oct 24, 2013
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AnyColumn {

    /**
     * A converter class to use when serializing/deserializing the date to/from
     * excel file. Converter class must implement
     * {@link com.ebay.xcelite.converters.ColumnValueConverter
     * ColumnValueConverter}. Default is no converter.
     */
    Class<? extends ColumnValueConverter<?, ?>> converter() default NoConverterClass.class;

    /**
     * Type to deserialize to. Default is {@link java.util.HashMap HashMap}.
     */
    @SuppressWarnings("rawtypes")
    Class<? extends Map> as() default HashMap.class;

    /**
     * Specifies which columns to ignore upon deserializtion. Ignored column will
     * not be added to the map.
     */
    String[] ignoreCols() default {};
}
