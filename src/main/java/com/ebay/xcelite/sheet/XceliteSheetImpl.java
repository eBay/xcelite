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
import com.ebay.xcelite.reader.*;
import com.ebay.xcelite.writer.*;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;
import java.util.Iterator;

/**
 * Wrapper class around a POI native sheet that represents one sheet
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
@Getter
public class XceliteSheetImpl implements XceliteSheet {
    private final Sheet nativeSheet;

    private XceliteOptions options;

    public XceliteSheetImpl(Sheet nativeSheet) {
        this.nativeSheet = nativeSheet;
        options = new XceliteOptions();
    }

    public XceliteSheetImpl(Sheet nativeSheet, XceliteOptions options) {
        this.nativeSheet = nativeSheet;
        if (null == options)
            this.options = new XceliteOptions();
        else
            this.options = new XceliteOptions(options);
    }

    @Override
    public SheetReader<Collection<Object>> getSimpleReader() {
        return new SimpleSheetReader(this, adaptDataRowIndex (options,0));
    }

    @Override
    public <T> SheetReader<T> getBeanReader(Class<T> type) {
        return new BeanSheetReader<>(this, adaptDataRowIndex (options, options.getHeaderRowIndex() + 1), type);
    }

    @Override
    public SimpleSheetWriter getSimpleWriter() {
        return new SimpleSheetWriter(this, adaptDataRowIndex (options, 0));
    }

    @Override
    public <T> SheetWriter<T> getBeanWriter(Class<T> type) {
        return new BeanSheetWriter<>(this, adaptDataRowIndex (options,options.getHeaderRowIndex() + 1), type);
    }

    public void setOptions(XceliteOptions options) {
        this.options = new XceliteOptions(options);
    }


    private XceliteOptions adaptDataRowIndex (XceliteOptions options, int newFirstDataRowIndex) {
        XceliteOptions lOptions = new XceliteOptions(options);
        if (lOptions.getFirstDataRowIndex() == -1)
            lOptions.setFirstDataRowIndex(newFirstDataRowIndex);
        return lOptions;
    }

    /*
     For readers/writers expecting a header-row:
     If the first data row setting from XceliteOptions is smaller than the
     setting for the header-row index, then assume the first data row is the row
     following the header row.
     */
    public static int getFirstDataRowIndex(DataMarshaller marshall) {
        XceliteOptions options = marshall.getOptions();
        boolean expectHeaderRow = marshall.expectsHeaderRow();
        if (null != options.isHasHeaderRow()) {
            expectHeaderRow = options.isHasHeaderRow();
        }
        int firstDataRowIndex = options.getFirstDataRowIndex();
        if ((expectHeaderRow) && (firstDataRowIndex <= options.getHeaderRowIndex())) {
            firstDataRowIndex = options.getHeaderRowIndex() +1;
        }
        return firstDataRowIndex;
    }

    /*
     For readers/writers expecting a header-row:
     If the first data row setting from XceliteOptions is smaller than the
     setting for the header-row index, then assume the first data row is the row
     following the header row.
     */
    @Override
    public Iterator<Row> moveToFirstDataRow(DataMarshaller marshall, boolean createRows) {
        int firstDataRowIndex = getFirstDataRowIndex(marshall);
        return skipRows (firstDataRowIndex, createRows);
    }

    @Override
    public Iterator<Row> moveToHeaderRow(int headerRowIndex, boolean createRows) {
        if (headerRowIndex <= 0) {
            return nativeSheet.rowIterator();
        }
        return skipRows (headerRowIndex, createRows);
    }

    /*
     Empty rows sadly are returned as null by POI and not contained in the rowiterator.
     Therefore, we need to find the last logical row that corresponds to the last skipped row.
     After that, iterate the row iterator as many times to skip lines and set it to the row
     before the wanted row. Seems clumsy, maybe think about a better way.
     */
    @Override
    public Iterator<Row> skipRows (int rowsToSkip, boolean createRows) {
        if ((rowsToSkip == 1) && (null == nativeSheet.getRow(0))) {
            return nativeSheet.rowIterator();
        }
        int lastRowNum = 0;
        for (int i = 0; i < rowsToSkip; i++) {
            Row r = nativeSheet.getRow(i);
            if (null != r)
                lastRowNum = r.getRowNum();
            else if (createRows) {
                r = nativeSheet.createRow(i);
                lastRowNum = r.getRowNum();
            }
        }
        Iterator<Row> rowIterator= nativeSheet.rowIterator();
        boolean stop = (lastRowNum >= rowsToSkip);
        while ((rowIterator.hasNext()) && (!stop)) {
            Row r = rowIterator.next();
            stop = (r.getRowNum() >= lastRowNum);
        }
        return rowIterator;
    }
}
