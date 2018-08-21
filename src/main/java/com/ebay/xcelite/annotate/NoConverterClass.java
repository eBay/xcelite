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
package com.ebay.xcelite.annotate;

import com.ebay.xcelite.converters.ColumnValueConverter;

/**
 * Marker class.
 *
 * @author kharel (kharel@ebay.com)
 * created Sep 12, 2013
 */
public final class NoConverterClass implements ColumnValueConverter<Object, Object> {

    @Override
    public Object serialize(Object value) {
        return value;
    }

    @Override
    public Object deserialize(Object value) {
        return value;
    }
}
