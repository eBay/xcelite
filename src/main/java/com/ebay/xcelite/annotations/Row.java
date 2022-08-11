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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used for configuring the bean class.
 *
 * @author kharel (kharel@ebay.com)
 * @since 1.0
 * created Aug 20, 2013
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Row {

    /**
     * Specifies the order for the Columns in excel file. The colsOrder is a list
     * of fields annotated with the {@link com.ebay.xcelite.annotations.Column
     * Column} annotation. Once specified, all Column fields must be listed.
     * @since 1.0
     */
    String[] colsOrder() default {};
}
