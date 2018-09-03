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

import com.ebay.xcelite.annotate.NoConverterClass;
import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.column.Col;
import com.ebay.xcelite.column.ColumnsExtractor;
import com.ebay.xcelite.converters.ColumnValueConverter;
import com.ebay.xcelite.exceptions.XceliteException;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.styles.CellStylesBank;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

import static org.reflections.ReflectionUtils.withName;

/**
 * An concrete implementation of the {@link SheetWriter} interface that writes
 * collections of annotated Java beans to Excel sheets.
 *
 * This writer class writes a header row as the first row in wich each cell
 * gets its text from the {@link Column} annotations of the Java bean.
 *
 * Preferably, this class should not directly be instantiated, but you should
 * call {@link XceliteSheet#getBeanWriter(Class)}
 *
 * By default, a SheetWriter copies over the {@link XceliteOptions options} from the sheet
 * it is constructed on. By this, the {@link com.ebay.xcelite.sheet.XceliteSheet} become the
 * default options, but the SheetWriter can modify option properties locally. However, the user
 * may use the {@link AbstractSheetWriter#AbstractSheetWriter(XceliteSheet, XceliteOptions)
 *    BeanSheetWriter(XceliteSheet, XceliteOptions)}
 * constructor to use - for one writer only - a completely different set of options from
 * the sheet options.
 *
 * @author kharel (kharel@ebay.com)
 * @since 1.0
 */

public class BeanSheetWriter<T> extends AbstractSheetWriter<T> {
    private final LinkedHashSet<Col> columns;
    private final Col anyColumn;
    private Row headerRow;
    private int rowIndex = 0;

    public BeanSheetWriter(XceliteSheet sheet, Class<T> type) {
        super(sheet);
        options.setGenerateHeaderRow(true);
        ColumnsExtractor extractor = new ColumnsExtractor(type);
        extractor.extract();
        columns = extractor.getColumns();
        anyColumn = extractor.getAnyColumn();
    }

    @Override
    public void write(Collection<T> data) {
        if (options.isGenerateHeaderRow()) {
            writeHeader();
        }
        writeData(data);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private void writeData(Collection<T> data) {
        Set<Col> columnsToAdd = new TreeSet();
        for (T t: data) {
            if (anyColumn != null) {
                appendAnyColumns(t, columnsToAdd);
            }
        }
        addColumns(columnsToAdd, true);
        for (T t: data) {
            Row row = sheet.getNativeSheet().createRow(rowIndex);
            int i = 0;
            for (Col col: columns) {
                Set<Field> fields = ReflectionUtils.getAllFields(t.getClass(), withName(col.getFieldName()));
                Field field = fields.iterator().next();
                field.setAccessible(true);
                Object fieldValueObj;
                if (col.isAnyColumn()) {
                    Map<String, Object> anyColumnMap = (Map<String, Object>) field.get(t);
                    fieldValueObj = anyColumnMap.get(col.getName());
                } else {
                    fieldValueObj = field.get(t);
                }
                Cell cell = row.createCell(i);
                writeToCell(cell, col, fieldValueObj);
                i++;
            }
            rowIndex++;
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private void writeToCell(Cell cell, Col col, Object fieldValueObj) {
        if (fieldValueObj == null) {
            cell.setCellValue((String) null);
            return;
        }
        if (col.getConverter() != null) {
            ColumnValueConverter<?, Object> converter = (ColumnValueConverter<?, Object>) col.getConverter().newInstance();
            fieldValueObj = converter.serialize(fieldValueObj);
        }
        if (col.getDataFormat() != null) {
            cell.setCellStyle(CellStylesBank.get(sheet.getNativeSheet().getWorkbook()).getCustomDataFormatStyle(
                    col.getDataFormat()));
        }

        if (col.getType().equals(Date.class)) {
            if (col.getDataFormat() == null) {
                cell.setCellStyle(CellStylesBank.get(sheet.getNativeSheet().getWorkbook()).getDateStyle());
            }
        }

        writeToCell(cell, fieldValueObj, col.getType());
    }

    private void writeHeader() {
        headerRow = sheet.getNativeSheet().createRow(rowIndex);
        rowIndex++;
        addColumns(columns, false);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private void appendAnyColumns(T t, Set<Col> columnToAdd) {
        Set<Field> fields = ReflectionUtils.getAllFields(t.getClass(), withName(anyColumn.getFieldName()));
        Field anyColumnField = fields.iterator().next();
        anyColumnField.setAccessible(true);
        Map<String, Object> fieldValueObj = (Map<String, Object>) anyColumnField.get(t);
        for (Map.Entry<String, Object> entry: fieldValueObj.entrySet()) {
            Col column = new Col(entry.getKey(), anyColumnField.getName());
            column.setType(entry.getValue() == null ? String.class : entry.getValue().getClass());
            column.setAnyColumn(true);
            if (!anyColumn.getConverter().equals(NoConverterClass.class)) {
                column.setConverter(anyColumn.getConverter());
            }
            columnToAdd.add(column);
        }
    }

    private void addColumns(Set<Col> columnsToAdd, boolean append) {
        CellStyle boldStyle = CellStylesBank.get(sheet.getNativeSheet().getWorkbook()).getBoldStyle();

        int i = (headerRow == null || headerRow.getLastCellNum() == -1) ? 0 : headerRow.getLastCellNum();
        for (Col column: columnsToAdd) {
            if (append && columns.contains(column))
                continue;
            if (options.isGenerateHeaderRow()) {
                if (headerRow == null)
                    throw new XceliteException("Cannot write header; header row is null");
                Cell cell = headerRow.createCell(i);
                cell.setCellType(CellType.STRING);
                cell.setCellStyle(boldStyle);
                cell.setCellValue(column.getName());
                i++;
            }
            columns.add(column);
        }
    }
}
