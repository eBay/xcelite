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
package com.ebay.xcelite.reader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.XceliteSheet;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Class description...
 *
 * @author kharel (kharel@ebay.com)
 * created Nov 11, 2013
 *
 */
public abstract class AbstractSheetReader<T> implements SheetReader<T> {
    @Getter
    protected final XceliteSheet sheet;

    final List<RowPostProcessor<T>> rowPostProcessors;

    @Getter
    protected final XceliteOptions options;


    public AbstractSheetReader(XceliteSheet sheet) {
        this (sheet, new XceliteOptions());
    }

    public AbstractSheetReader(XceliteSheet sheet, XceliteOptions options) {
        this.sheet = sheet;
        this.options = options;
        rowPostProcessors = new ArrayList<>();
    }

    /**
     * @deprecated since 1.2 use the constructor using {@link XceliteOptions}
     * and set {@link XceliteOptions#setSkipRowsBeforeColumnDefinitionRow(Integer) setSkipLinesBeforeHeader}
     * to 1
     * @param sheet The sheet to read from
     * @param skipHeaderRow whether or not one header row should be skipped
     */
    @Deprecated
    public AbstractSheetReader(XceliteSheet sheet, boolean skipHeaderRow) {
        this (sheet);
        if (skipHeaderRow)
            options.setSkipRowsBeforeColumnDefinitionRow(1);
    }

    public static Object readValueFromCell(Cell cell) {
        if (cell == null) return null;
        Object cellValue = null;
        switch (cell.getCellTypeEnum()) {
            case ERROR:
                break;
            case FORMULA:
                // Get the type of Formula
                switch (cell.getCachedFormulaResultTypeEnum()) {
                    case ERROR:
                        cellValue = null;
                        break;
                    case STRING:
                        cellValue = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        cellValue = cell.getNumericCellValue();
                        break;
                    default:
                        cellValue = cell.getStringCellValue();
                }
                break;
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            default:
                cellValue = cell.getStringCellValue();
        }
        return cellValue;
    }

    /**
     * @deprecated since 1.2. Use {@link #getOptions()} instead and set
     * {@link XceliteOptions#setSkipRowsBeforeColumnDefinitionRow(Integer) setSkipLinesBeforeHeader}
     * to 1
     * @param skipHeaderRow true to skip the header row, false otherwise
     */
    @Override
    @Deprecated
    public void skipHeaderRow(boolean skipHeaderRow) {
        if (skipHeaderRow)
            options.setSkipRowsBeforeColumnDefinitionRow(1);
    }

    @Override
    public void addRowPostProcessor(RowPostProcessor<T> rowPostProcessor) {
        rowPostProcessors.add(rowPostProcessor);
    }

    @Override
    public void removeRowPostProcessor(RowPostProcessor<T> rowPostProcessor) {
        rowPostProcessors.remove(rowPostProcessor);
    }

    boolean isBlankRow(Row row) {
        Iterator<Cell> cellIterator = row.cellIterator();
        boolean blankRow = true;
        while (cellIterator.hasNext()) {
            Object value = readValueFromCell(cellIterator.next());
            if (blankRow && value != null && !String.valueOf(value).isEmpty()) {
                blankRow = false;
            }
        }
        return blankRow;
    }

    boolean shouldKeepObject(T object, List<RowPostProcessor<T>> rowPostProcessors) {
        boolean keepObject = true;

        for (RowPostProcessor<T> rowPostProcessor: rowPostProcessors) {
            keepObject = rowPostProcessor.process(object);
            if (!keepObject) break;
        }

        return keepObject;
    }

    static Iterator<Row> skipRowsAfterColumnDefinition(Sheet nativeSheet, XceliteOptions options) {
        if (options.getSkipRowsAfterColumnDefinitionRow() < 0)
            return nativeSheet.rowIterator();
        return skipRows (nativeSheet,
                options.getSkipRowsBeforeColumnDefinitionRow()
                        + options.getSkipRowsAfterColumnDefinitionRow()
                        + 1);
    }

    static Iterator<Row> moveToFirstRow(Sheet nativeSheet, XceliteOptions options) {
        if (options.getSkipRowsBeforeColumnDefinitionRow() <= 0)
            return nativeSheet.rowIterator();
        return skipRows (nativeSheet, options.getSkipRowsBeforeColumnDefinitionRow());
    }

    /*
     Empty rows sadly are returned as null. Therefore, we need to find the  last  logical row
     that corresponds to the last skipped row.
     After that, iterate the row iterator as many times to skip lines and set it to the row
     before the wanted row. Seems clumsy, maybe think about a better way.
     */
    static Iterator<Row> skipRows (Sheet nativeSheet, int rowsToSkip) {
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
