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

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

/**
 * An abstraction of delimiter column value converter. Implemented by
 * {@link com.ebay.xcelite.converters.CSVColumnValueConverter CSVColumnValueConverter}
 * 
 * @author kharel (kharel@ebay.com)
 * created Sep 14, 2013
 * 
 */
public abstract class DelimiterColumnValueConverter implements ColumnValueConverter<String, Collection<?>> {

  @Override
  public String serialize(Collection<?> value) {
    return Joiner.on(getDelimiter()).skipNulls().join(value);
  }

  @Override
  public Collection<?> deserialize(String value) {
    Iterable<String> split = Splitter.on(getDelimiter()).omitEmptyStrings().trimResults().split(value);
    return getCollection(split);
  }

  /**
   * Gets the string separator.
   * 
   * @return the delimiter string
   */
  protected abstract String getDelimiter();
  
  /**
   * Gets the collection that will be used to store the deserialized delimited string.
   * 
   * @param iterable an iteration over the segments split
   * @return a collection generated from the splitted string iterable
   */
  protected abstract Collection<?> getCollection(Iterable<?> iterable);
}
