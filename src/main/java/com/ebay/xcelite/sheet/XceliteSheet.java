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

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.styles.CellStylesBank;
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
public interface XceliteSheet<N> {

    /**
     * Return a {@link com.ebay.xcelite.writer.BeanSheetWriter} that allows
     * for serializing collections of Bean row objects to the underlying
     * native sheet.
     *
     * @param type class of the row Beans.
     */
    <T> SheetWriter<T> getBeanWriter(Class<T> type);

    /**
     * Return a {@link com.ebay.xcelite.reader.BeanSheetReader} that allows
     * for reading the underlying native sheet as a collection of row, each
     * row being returned as a row Bean.
     *
     * @return Reader over the rows of this sheet
     */
    <T> SheetReader<T> getBeanReader(Class<T> type);

    /**
     * Return a {@link com.ebay.xcelite.writer.SimpleSheetWriter} that allows
     * for serializing collections of row objects to the underlying
     * native sheet, each row being a collection of object.
     */
    SheetWriter<Collection<Object>> getSimpleWriter();

    /**
     * Return a {@link com.ebay.xcelite.reader.SimpleSheetReader} that allows
     * for reading the underlying native sheet as a collection of rows, each
     * row being returned as a collection of object.
     *
     * @return Reader over the rows of this sheet
     */
    SheetReader<Collection<Object>> getSimpleReader();

    /**
     * Return a `RowIterator` over the underlying Excel sheet
     * @return An iterator over `Row`s
     *
     * @since 1.3
     */
    Iterator<Row> getRowIterator();

    /**
     * Return a `RowIterator` that is already skipped over head and leading
     * blank rows so the next call to `next()` will return the first data
     * row
     * @param marshall DataMarshaller that determines the location of the
     *                 header row
     * @return An iterator over `Row`s
     *
     * @since 1.3
     */
    Iterator<Row> getDataRowsIterator(DataMarshaller marshall);

    /**
     * Create and insert a number of empty rows from index `fromRowNum`
     * to `toRowNum`
     * @param fromRowNum first row index to create
     * @param toRowNum last row index to create
     * @return a collection holding the newly created rows.
     *
     * @since 1.3
     */
    Collection<Row> createRowsUptoAndIncludingRow(int fromRowNum, int toRowNum);

    /**
     * Return a row at position rowNum. if parameter `createRow` is set to `true`,
     * create and insert an empty row
     * @param rowNum row index to create
     * @param createRow whether to return a missing row or create and insert it
     * @return the newly created row or null if `createRow` is set to `false` and
     * an empty row was encountered
     *
     * @since 1.3
     */
    Row getOrCreateRow(int rowNum, boolean createRow);

    /**
     * Return the underlying native spreadsheet object
     * @return the native spreadsheet object his object is built upon.
     */
    N getNativeSheet();

    /**
     * Returns the sheet name. This is returning the name of the underlying
     * native sheet.
     * @return name of the native sheet.
     *
     * @since 1.3
     */
    String getSheetName();

    /**
     * Returns the row index of the last row, regardless of empty or filled
     * @return row index of the last row
     *
     * @since 1.3
     */
    int getLastRowNumber();

    /**
     * Returns the {@link com.ebay.xcelite.options.XceliteOptions} set on this
     * sheet
     * @return returns sheet options
     *
     * @since 1.2
     */
    XceliteOptions getOptions();

    /**
     * Sets {@link com.ebay.xcelite.options.XceliteOptions} on this
     * sheet
     * @param options sheet options to set
     *
     * @since 1.2
     */
    void setOptions(XceliteOptions options);

    /**
     * Returns the styles available for use on this sheet
     * @return the styles of this sheet
     *
     * @since 1.3
     */
    CellStylesBank getStyles();

}
