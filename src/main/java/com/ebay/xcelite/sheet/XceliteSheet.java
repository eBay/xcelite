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
package com.ebay.xcelite.sheet;

import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.writer.SheetWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;
import java.util.Iterator;

/**
 * Interface to a class around a POI native sheet that represents one sheet
 * of an Excel workbook. User can request a {@link SheetReader} or
 * {@link SheetWriter} from it to read or write to/from the underlying
 * Excel sheet. Readers and Writers inherit the XceliteSheet's
 * {@link XceliteOptions options}.
 *
 * Users should not use constructors to get a XceliteSheet, but use
 * the methods in {@link com.ebay.xcelite.Xcelite} to retrieve or
 * create XceliteSheets on their workbooks.
 *
 * @author kharel (kharel@ebay.com)
 * @since 1.0
 * created Nov 9, 2013
 */
public interface XceliteSheet {

    <T> SheetWriter<T> getBeanWriter(Class<T> type);

    <T> SheetReader<T> getBeanReader(Class<T> type);

    SheetWriter<Collection<Object>> getSimpleWriter();

    SheetReader<Collection<Object>> getSimpleReader();

    Iterator<Row> moveToFirstDataRow(XceliteOptions options, boolean createRows);

    Iterator<Row> moveToHeaderRow(int headerRowIndex, boolean createRows);

    Iterator<Row> skipRows (int rowsToSkip, boolean createRows);

    Sheet getNativeSheet();

    XceliteOptions getOptions();

    void setOptions(XceliteOptions options);
}
