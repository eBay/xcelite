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
import com.ebay.xcelite.reader.BeanSheetReader;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.reader.SimpleSheetReader;
import com.ebay.xcelite.writer.BeanSheetWriter;
import com.ebay.xcelite.writer.SheetWriter;
import com.ebay.xcelite.writer.SimpleSheetWriter;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;
import java.util.Iterator;

/**
 * Class description...
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

    private XceliteOptions adaptDataRowIndex (int newFirstDataRowIndex) {
        XceliteOptions lOptions = new XceliteOptions(options);
        if (lOptions.getFirstDataRowIndex() == -1)
            lOptions.setFirstDataRowIndex(newFirstDataRowIndex);
        return lOptions;
    }

    @Override
    public <T> SheetWriter<T> getBeanWriter(Class<T> type) {
        return new BeanSheetWriter<>(this, adaptDataRowIndex (options.getHeaderRowIndex() + 1), type);
    }

    @Override
    public <T> SheetReader<T> getBeanReader(Class<T> type) {
        return new BeanSheetReader<>(this, adaptDataRowIndex (options.getHeaderRowIndex() + 1), type);
    }

    @Override
    public SimpleSheetWriter getSimpleWriter() {
        return new SimpleSheetWriter(this, adaptDataRowIndex (options.getHeaderRowIndex()));
    }

    @Override
    public SheetReader<Collection<Object>> getSimpleReader() {
        return new SimpleSheetReader(this, adaptDataRowIndex (options.getHeaderRowIndex()));
    }

    public void setOptions(XceliteOptions options) {
        this.options = new XceliteOptions(options);
    }


    /*
    If the first data row setting from XceliteOptions is the default (-1) AND
    we have an explicit setting for the header-row index, then assume the first
    data row is the row following the header row
     */
    @Override
    public Iterator<Row> moveToFirstDataRow(XceliteOptions options, boolean createRows) {
        int firstDataRowIndex = options.getFirstDataRowIndex();
        if (firstDataRowIndex < 0) {
            if (options.getHeaderRowIndex() != 0)
                firstDataRowIndex = options.getHeaderRowIndex() +1;
        }
        return skipRows (firstDataRowIndex, createRows);
    }

    @Override
    public Iterator<Row> moveToHeaderRow(XceliteOptions options, boolean createRows) {
        if (options.getHeaderRowIndex() <= 0)
            return nativeSheet.rowIterator();
        return skipRows (options.getHeaderRowIndex(), createRows);
    }

    /*
     Empty rows sadly are returned as null. Therefore, we need to find the  last  logical row
     that corresponds to the last skipped row.
     After that, iterate the row iterator as many times to skip lines and set it to the row
     before the wanted row. Seems clumsy, maybe think about a better way.
     */
    @Override
    public Iterator<Row> skipRows (int rowsToSkip, boolean createRows) {
        int lastRowNum = 0;
        for (int i = 0; i < rowsToSkip; i++) {
            Row r = nativeSheet.getRow(i);
            if (null != r)
                lastRowNum = r.getRowNum();
        }
        Iterator<Row> rowIterator= nativeSheet.rowIterator();
        boolean stop = false;
        while ((rowIterator.hasNext()) && (!stop)) {
            Row r = rowIterator.next();
            stop = (r.getRowNum() >= lastRowNum);
        }
        return rowIterator;
    }
}
