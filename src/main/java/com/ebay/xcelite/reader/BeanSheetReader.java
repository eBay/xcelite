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

import com.ebay.xcelite.annotate.NoConverterClass;
import com.ebay.xcelite.annotations.AnyColumn;
import com.ebay.xcelite.column.Col;
import com.ebay.xcelite.column.ColumnsExtractor;
import com.ebay.xcelite.column.ColumnsMapper;
import com.ebay.xcelite.converters.ColumnValueConverter;
import com.ebay.xcelite.exceptions.ColumnNotFoundException;
import com.ebay.xcelite.exceptions.XceliteException;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

import static org.reflections.ReflectionUtils.withName;

/**
 * Class description...
 *
 * @author kharel (kharel@ebay.com)
 * created Sep 9, 2013
 */
public class BeanSheetReader<T> extends SheetReaderAbs<T> {

    private final LinkedHashSet<Col> columns;
    private final Col anyColumn;
    private final ColumnsMapper mapper;
    private final Class<T> type;
    private ArrayList<String> header;
    private Iterator<Row> rowIterator;

    public BeanSheetReader(XceliteSheet sheet, Class<T> type) {
        super(sheet, false);
        this.type = type;
        ColumnsExtractor extractor = new ColumnsExtractor(type);
        extractor.extract();
        columns = extractor.getColumns();
        anyColumn = extractor.getAnyColumn();
        mapper = new ColumnsMapper(columns);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SneakyThrows
    public Collection<T> read() {
        buildHeader();
        if (anyColumn == null) {
            validateColumns();
        }
        List<T> data = Lists.newArrayList();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isBlankRow(row)) continue;
            T object = type.newInstance();

            int i = 0;
            for (String columnName: header) {
                Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                Col col = mapper.getColumn(columnName);
                if (col == null) {
                    if (anyColumn != null) {
                        Set<Field> fields = ReflectionUtils.getAllFields(object.getClass(), withName(anyColumn.getFieldName()));
                        Field field = fields.iterator().next();
                        if (!isColumnInIgnoreList(field, columnName)) {
                            writeToAnyColumnField(field, object, cell, columnName);
                        }
                    }
                } else {
                    Set<Field> fields = ReflectionUtils.getAllFields(object.getClass(), withName(col.getFieldName()));
                    Field field = fields.iterator().next();
                    writeToField(field, object, cell, col);
                }
                i++;
            }
            if (shouldKeepObject(object, rowPostProcessors)) {
                data.add(object);
            }
        }

        return data;
    }

    /**
     * check that @column is found in header
     */
    private void validateColumns() {
        boolean found = false;
        for (Col c: columns) {
            for (String h: header) {
                if (c.getName().equals(h)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new ColumnNotFoundException("Column not found!", c.getName());
            }
            found = false;
        }
    }

    private boolean isBlankRow(Row row) {
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

    private static boolean isColumnInIgnoreList(Field anyColumnField, String columnName) {
        AnyColumn annotation = anyColumnField.getAnnotation(AnyColumn.class);
        Set<String> ignoreCols = Sets.newHashSet(annotation.ignoreCols());
        return ignoreCols.contains(columnName);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private void writeToAnyColumnField(Field field, T object, Cell cell, String columnName) {
        field.setAccessible(true);
        Object value = readValueFromCell(cell);
        if (value != null) {
            AnyColumn annotation = field.getAnnotation(AnyColumn.class);
            if (field.get(object) == null) {
                Map<String, Object> map = (Map<String, Object>) annotation.as().newInstance();
                field.set(object, map);
            }
            Map<String, Object> map = (Map<String, Object>) field.get(object);
            if (annotation.converter() != NoConverterClass.class) {
                ColumnValueConverter<Object, ?> converter = (ColumnValueConverter<Object, ?>) annotation.converter()
                        .newInstance();
                value = converter.deserialize(value);
            }
            map.put(columnName, value);
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private void writeToField(Field field, T object, Cell cell, Col column) {
        Object cellValue = readValueFromCell(cell);
        if (cellValue == null && (field.getType() == Boolean.class || field.getType() == boolean.class)) {
            cellValue = Boolean.FALSE;
        }
        if (cellValue != null) {
            if (column.getConverter() != null) {
                ColumnValueConverter<Object, ?> converter = (ColumnValueConverter<Object, ?>) column.getConverter()
                        .newInstance();
                cellValue = converter.deserialize(cellValue);
            } else {
                cellValue = convertToFieldType(cellValue, field.getType());
            }
            field.setAccessible(true);
            field.set(object, cellValue);
        }
    }

    private boolean shouldKeepObject(T object, List<RowPostProcessor<T>> rowPostProcessors) {
        boolean keepObject = true;

        for (RowPostProcessor<T> rowPostProcessor: rowPostProcessors) {
            keepObject = rowPostProcessor.process(object);
            if (!keepObject) break;
        }

        return keepObject;
    }

    private static Object convertToFieldType(Object cellValue, Class<?> fieldType) {
        String value = String.valueOf(cellValue);
        if (fieldType == Double.class || fieldType == double.class) {
            return Double.valueOf(value);
        }
        if (fieldType == Integer.class || fieldType == int.class) {
            return Double.valueOf(value).intValue();
        }
        if (fieldType == Short.class || fieldType == short.class) {
            return Double.valueOf(value).shortValue();
        }
        if (fieldType == Long.class || fieldType == long.class) {
            return Double.valueOf(value).longValue();
        }
        if (fieldType == Float.class || fieldType == float.class) {
            return Double.valueOf(value).floatValue();
        }
        if (fieldType == Character.class || fieldType == char.class) {
            return value.charAt(0);
        }
        if (fieldType == Date.class) {
            return DateUtil.getJavaDate(Double.valueOf(value));
        }
        if (fieldType == Boolean.class || fieldType == boolean.class) {
            return Boolean.valueOf(value);
        }
        return value;
    }

    private void buildHeader() {
        header = Lists.newArrayList();
        rowIterator = sheet.getNativeSheet().rowIterator();
        Row row = rowIterator.next();
        if (row == null) {
            throw new XceliteException("First row in sheet is empty. First row must contain header");
        }

        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            Cell cell = row.getCell(i);
            String cellValue = (null != cell) ? cell.getStringCellValue() : null;
            if ((null == cellValue) || (cellValue.isEmpty()))
                cellValue = null;
            header.add(cellValue);
        }
    }
}
