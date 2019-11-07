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
import com.ebay.xcelite.styles.CellStylesBank;
import com.ebay.xcelite.writer.*;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
public class XceliteSheetImpl implements XceliteSheet<Sheet> {
    private final Sheet nativeSheet;

    private XceliteOptions options;

    @Getter
    protected CellStylesBank styles;

    public XceliteSheetImpl(Sheet nativeSheet) {
        this(nativeSheet, new XceliteOptions());
    }

    public XceliteSheetImpl(Sheet nativeSheet, XceliteOptions options) {
        this.nativeSheet = nativeSheet;
        if (null == options)
            this.options = new XceliteOptions();
        else
            this.options = new XceliteOptions(options);
        styles = new CellStylesBank(nativeSheet.getWorkbook());
    }

    @Override
    public SheetReader<Collection<Object>> getSimpleReader() {
        return new SimpleSheetReader(this, adaptDataRowIndex (options,0));
    }

    @Override
    public Iterator<Row> getRowIterator() {
        return new RowIterator();
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

    @Override
    public void setOptions(XceliteOptions options) {
        this.options = new XceliteOptions(options);
    }


    private static XceliteOptions adaptDataRowIndex(XceliteOptions options, int newFirstDataRowIndex) {
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

    /**
     * Return a `RowIterator` that is already skipped over head and leading
     * blank rows so the next call to `next()` will return the first data
     * row
     * @param marshall DataMarshaller that determines the location of the
     *                 header row
     * @return An iterator over `Row`s
     */
    @Override
    public Iterator<Row> getDataRowsIterator(DataMarshaller marshall) {
        RowIterator iter = new RowIterator();
        int firstDataRowIndex = getFirstDataRowIndex(marshall);
        if (firstDataRowIndex == 0)
            return iter;
        return iter.skipToBeforeRow(firstDataRowIndex);
    }

    @Override
    public Collection<Row> createRowsUptoAndIncludingRow(int fromRowNum, int toRowNum) {
        if (toRowNum < fromRowNum)
            throw new IllegalArgumentException("Last row index cannot be lower than first " +
                    "row index");
        List<Row> retVal = new ArrayList<>();
        Row r;
        for (int i = fromRowNum; i <= toRowNum; i++) {
            r = getOrCreateRow(i, true);
            retVal.add(r);
        }
        return retVal;
    }

    @Override
    public Row getOrCreateRow(int rowNum, boolean createRows) {
        Row r = nativeSheet.getRow(rowNum);
        if (null != r)
            return r;
        else if (createRows) {
            return nativeSheet.createRow(rowNum);
        }
        return null;
    }

    @Override
    public String getSheetName() {
        return nativeSheet.getSheetName();
    }

    @Override
    public int getLastRowNumber() {
        return nativeSheet.getLastRowNum()+1;
    }

    public class RowIterator implements Iterator<Row> {
        private int currentRow = 0;
        private int lastRow;

        RowIterator() {
            Sheet sheet = XceliteSheetImpl.this.getNativeSheet();
            Row lastExcelRow = sheet.getRow(sheet.getLastRowNum());
            lastRow = lastExcelRow.getRowNum();
        }

        @Override
        public boolean hasNext() {
            return lastRow >= currentRow;
        }

        @Override
        public Row next() {
            return XceliteSheetImpl.this.getNativeSheet().getRow(currentRow++);
        }

        Iterator<Row> skipToBeforeRow(int skipToBeforeRowNum) {
            if (currentRow >= skipToBeforeRowNum)
                throw new IllegalStateException("Already after row "+skipToBeforeRowNum);
            if (skipToBeforeRowNum <= 0)
                return this;
            while (hasNext() && (currentRow < skipToBeforeRowNum)) {
                next();
            }
            return this;
        }
    }
}
