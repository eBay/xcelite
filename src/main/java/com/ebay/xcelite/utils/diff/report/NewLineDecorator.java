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
package com.ebay.xcelite.utils.diff.report;

import java.util.Collection;

/**
 * Class description...
 *
 * @author kharel (kharel@ebay.com)
 * @creation_date Nov 20, 2013
 * 
 */
public class NewLineDecorator<T> {
  
  private static final String NEW_LINE = System.getProperty("line.separator");
  
  private final Collection<T> collection;
  
  public NewLineDecorator(Collection<T> collection) {
    this.collection = collection;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (T t : collection) {
      sb.append(t + NEW_LINE);      
    }
    return sb.toString();
  }
}
