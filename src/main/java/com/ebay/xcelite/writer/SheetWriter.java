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


import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.DataMarshaller;
import com.ebay.xcelite.sheet.XceliteSheet;
import org.apache.poi.ss.usermodel.Row;

import java.util.Collection;
/**
 * Generic interface for writer classes that can serialize collections of Java objects
 * to Excel workbooks.
 *
 * @author kharel (kharel@ebay.com)
 * @since 1.0
 * created Sep 12, 2013
 */
public interface SheetWriter<T> extends DataMarshaller {

    /**
     * Takes a collection of the specified type and writes it to the
     * {@link XceliteSheet} object this writer is operating on.
     *
     * @param data of the specified type
     * @since 1.0
     */
    void write(Collection<T> data);

    /**
     * Takes one instance of the specified type and writes it to the
     * {@link XceliteSheet} object this writer is operating on.
     *
     * @param data of the specified type
     * @param excelRow the row object in the spreadsheet to write to
     * @param rowIndex row index of the row object in the spreadsheet to write to
     * @since 1.0
     */
    void writeRow(T data, Row excelRow, int rowIndex);

    /**
     * Determines whether this writer will generate a header row with the
     * column defining attributes from the {@link com.ebay.xcelite.annotations.Column}
     * annotations on the bean's properties
     *
     * @param generateHeaderRow if set to `true`, generate a header row
     * @deprecated since 1.2. Use {@link XceliteOptions#setHasHeaderRow(boolean)} instead}
     */
    @Deprecated
    void generateHeaderRow(boolean generateHeaderRow);

    /**
     * Determines whether this writer will generate a header row with the
     * column defining attributes from the {@link com.ebay.xcelite.annotations.Column}
     * annotations on the bean's properties
     *
     * @param generateHeaderRow if set to `true`, generate a header row
     * @deprecated since 1.2. Use {@link XceliteOptions#setHasHeaderRow(boolean)} instead}
     */
    @Deprecated
    void setGenerateHeaderRow(boolean generateHeaderRow);

    /**
     * Gets the {@link XceliteSheet} object this writer is operating on
     * @return sheet this writer writes to.
     */
    XceliteSheet getSheet();


}
