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
package com.ebay.xcelite.converters;

import java.util.Collection;

import com.google.common.collect.Lists;

/**
 * Serializes a Collection object to a comma separated String. Deserializes a
 * comma separated String to an ArrayList. If a different Collection is required
 * other than {@link java.util.ArrayList ArrayList}, extend this class to
 * override {@link #getCollection(Iterable)} method.
 * 
 * @author kharel (kharel@ebay.com)
 * @creation_date Sep 14, 2013
 */
public class CSVColumnValueConverter extends DelimiterColumnValueConverter {

  @Override
  protected String getDelimiter() {
    return ",";
  }

  @Override
  protected Collection<?> getCollection(Iterable<?> iterable) {
    return Lists.newArrayList(iterable);
  }
}
