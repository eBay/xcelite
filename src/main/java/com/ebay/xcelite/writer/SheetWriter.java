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
package com.ebay.xcelite.writer;


import com.ebay.xcelite.sheet.XceliteSheet;

import java.util.Collection;
/**
 * Generic interface for writer classes that can serialize collections of Java objects
 * to Excel workbooks.
 *
 * @author kharel (kharel@ebay.com)
 * @since 1.0
 * created Sep 12, 2013
 */
public interface SheetWriter<T> {

    void write(Collection<T> data);

    /**
     * @deprecated since 1.2. Use {@link #setGenerateHeaderRow(boolean) instead}
     */
    @Deprecated
    void generateHeaderRow(boolean generateHeaderRow);

    void setGenerateHeaderRow(boolean generateHeaderRow);

    XceliteSheet getSheet();
}
