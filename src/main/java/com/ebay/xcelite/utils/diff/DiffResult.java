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
package com.ebay.xcelite.utils.diff;

import java.util.Collection;

/**
 * Class description...
 *
 * @author kharel (kharel@ebay.com)
 * created Nov 20, 2013
 */
public interface DiffResult<T> {

    /**
     * Whether or not the two sheets are identical.
     *
     * @return true if both sheets are identical, false otherwise
     */
    boolean isIdentical();

    /**
     * Gets a collection which represents the difference between two sheets.
     *
     * @return the diff collection. If there is no difference, collection is returned empty
     */
    Collection<T> getDifference();

    String getReport();
}
