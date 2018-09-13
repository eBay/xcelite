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

/**
 * Annotation to annotate a field to represent a column in excel file.
 *
 * @author kharel (kharel@ebay.com)
 * @since 1.0
 * created Aug 29, 2013
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Column {

    /**
     * The actual name of the column that will be written to excel file. If no
     * name specified, the annotated field name will be taken.
     */
    String name() default "";

    /**
     * If true, ignores the actual field type and serializes the field value as
     * String. Otherwise, uses the actual field type when writing the data.
     */
    boolean ignoreType() default false;

    /**
     * The cell format to use when writing the data to excel file. Default is no
     * format.
     */
    String dataFormat() default "";

    /**
     * Converter class to use when serializing/deserializing the data. Class must
     * implement
     * {@link com.ebay.xcelite.converters.ColumnValueConverter
     * ColumnValueConverter}. Default is no converter.
     */
    Class<? extends ColumnValueConverter<?, ?>> converter() default NoConverterClass.class;
}
