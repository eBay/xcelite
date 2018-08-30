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
import com.ebay.xcelite.converters.ColumnValueConverter;
import com.ebay.xcelite.exceptions.ColumnNotFoundException;
import com.ebay.xcelite.exceptions.EmptyCellException;
import com.ebay.xcelite.exceptions.EmptyRowException;
import com.ebay.xcelite.exceptions.XceliteException;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.MissingCellPolicy;
import com.ebay.xcelite.policies.MissingRowPolicy;
import com.ebay.xcelite.sheet.XceliteSheet;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.withName;

/**
 * Implementation of the {@link SheetReader} interface that returns the contents
 * of an Excel sheet as a {@link java.util.Collection}. Each entry into that Collection
 * represents one row in the Excel sheet.
 *
 * @author kharel (kharel@ebay.com)
 * created Sep 9, 2013
 */
public class BeanSheetReader<T> extends AbstractSheetReader<T> {

    private final LinkedHashSet<Col> declaredColumns;
    private final Col anyColumn;
    private final ColumnsMapper mapper;
    private final Class<T> type;
    private Map<Integer, String> headerColumns;
    private Iterator<Row> rowIterator;

    public BeanSheetReader(XceliteSheet sheet, XceliteOptions options, Class<T> type) {
        super(sheet, options);
        this.type = type;
        ColumnsExtractor extractor = new ColumnsExtractor(type);
        extractor.extract();
        declaredColumns = extractor.getColumns();
        anyColumn = extractor.getAnyColumn();
        mapper = new ColumnsMapper(declaredColumns);
    }

    /**
     * Construct a BeanSheetReader with default options
     * @param sheet the {@link XceliteSheet} to read from
     * @param type class of the beans
     */
    public BeanSheetReader(XceliteSheet sheet, Class<T> type) {
        this(sheet, new XceliteOptions(), type);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SneakyThrows
    public Collection<T> read() {
        List<T> data = new ArrayList<>();

        Sheet s = sheet.getNativeSheet();
        rowIterator = moveToFirstRow(s, options);
        if (!rowIterator.hasNext())
            return data;

        buildHeader();
        validateColumns();
        rowIterator = skipRowsAfterColumnDefinition(s, options);

        rowIterator.forEachRemaining(excelRow -> {
            T object;

            if (isBlankRow(excelRow)) {
                switch (options.getMissingRowPolicy()) {
                    case THROW:
                        throw new EmptyRowException();
                    case RETURN_EMPTY_OBJECT:
                        object = fillObject(excelRow);
                        break;
                    case RETURN_NULL:
                        object = null;
                        break;
                    default:
                        object = null;
                }
                if (!options.getMissingRowPolicy().equals(MissingRowPolicy.SKIP)) {
                    if (shouldKeepObject(object, rowPostProcessors)) {
                        data.add(object);
                    }
                }
            } else {
                object = fillObject(excelRow);
                if (shouldKeepObject(object, rowPostProcessors)) {
                    data.add(object);
                }
            }
        });

        return data;
    }

    @SneakyThrows
    private T fillObject(Row row) {
        T object = type.newInstance();

        for (int i = 0; i < headerColumns.keySet().size(); i++) {
            String columnName = headerColumns.get(i);
            validateCellAgainstMissingCellPolicy(row, i);
            Cell cell = row.getCell(i, MissingCellPolicy.toPoiMissingCellPolicy(options.getMissingCellPolicy()));

            Col col = mapper.getColumn(columnName);
            if (col != null) {
                Field field = getField(object.getClass(), col.getFieldName());
                writeToField(field, object, cell, col);
            } else {
                if (anyColumn != null) {
                    Field field = getField(object.getClass(), anyColumn.getFieldName());
                    if (!isColumnInIgnoreList(field, columnName)) {
                        writeToAnyColumnField(field, object, cell, columnName);
                    }
                }
            }
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    private Field getField(Class<?> aClass, String name) {
        return ReflectionUtils.getAllFields(aClass, withName(name)).iterator().next();
    }

    /**
     * check that every @column is found in header
     */
    private void validateColumns() {
        if (anyColumn != null) {
            return;
        }
        Collection<String> declaredHeaders = mapper
                .getColumnsMap()
                .values()
                .stream()
                .map(c -> c.getName())
                .collect(Collectors.toSet());
        Collection<String> headers = headerColumns.values();
        if (!headers.containsAll(declaredHeaders)) {
            throw new ColumnNotFoundException(declaredHeaders.iterator().next());
        }
    }

    private void validateCellAgainstMissingCellPolicy(Row row, int colIdx) {
        MissingCellPolicy policy = options.getMissingCellPolicy();
        if (policy.equals(MissingCellPolicy.THROW)) {
            Cell cell = row.getCell(colIdx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (null == cell) {
                throw new EmptyCellException(headerColumns.get(colIdx));
            }
        }
    }

    private static boolean isColumnInIgnoreList(Field anyColumnField, String columnName) {
        AnyColumn annotation = anyColumnField.getAnnotation(AnyColumn.class);
        Set<String> ignoreCols = Arrays
                .stream(annotation.ignoreCols())
                .collect(Collectors.toSet());
        return ignoreCols.contains(columnName);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private void writeToAnyColumnField(Field field, T object, Cell cell, String columnName) {
        field.setAccessible(true);
        Object cellValue = readValueFromCell(cell);
        if (cellValue == null && (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class))) {
            cellValue = Boolean.FALSE;
        }
        AnyColumn annotation = field.getAnnotation(AnyColumn.class);
        if (field.get(object) == null) {
            Map<String, Object> map = (Map<String, Object>) annotation.as().newInstance();
            field.set(object, map);
        }
        Map<String, Object> map = (Map<String, Object>) field.get(object);

        if (cellValue != null) {
            if (!annotation.converter().equals(NoConverterClass.class)) {
                ColumnValueConverter<Object, ?> converter =
                        (ColumnValueConverter<Object, ?>) annotation.converter().newInstance();
                cellValue = converter.deserialize(cellValue);
            }
        }
        map.put(columnName, cellValue);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private void writeToField(Field field, T object, Cell cell, Col column) {
        Object cellValue = readValueFromCell(cell);
        if (cellValue == null && (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class))) {
            cellValue = Boolean.FALSE;
        }
        if (cellValue != null) {
            if (column.getConverter() != null) {
                ColumnValueConverter<Object, ?> converter =
                        (ColumnValueConverter<Object, ?>) column.getConverter().newInstance();
                cellValue = converter.deserialize(cellValue);
            } else {
                cellValue = convertToFieldType(cellValue, field.getType());
            }
            field.setAccessible(true);
            field.set(object, cellValue);
        }
    }

    private static Object convertToFieldType(Object cellValue, Class<?> fieldType) {
        String value = String.valueOf(cellValue);
        if ((fieldType.equals(Double.class)) || (fieldType.equals(double.class))) {
            return Double.valueOf(value);
        }
        if ((fieldType.equals(Integer.class)) || (fieldType.equals(int.class))) {
            return Double.valueOf(value).intValue();
        }
        if ((fieldType.equals(Short.class)) || (fieldType.equals(short.class))) {
            return Double.valueOf(value).shortValue();
        }
        if ((fieldType.equals(Long.class)) || (fieldType.equals(long.class))) {
            return Double.valueOf(value).longValue();
        }
        if ((fieldType.equals(Float.class)) || (fieldType.equals(float.class))) {
            return Double.valueOf(value).floatValue();
        }
        if ((fieldType.equals(Character.class)) || (fieldType.equals(char.class))) {
            return value.charAt(0);
        }
        if (fieldType.equals(Date.class)) {
            return DateUtil.getJavaDate(Double.valueOf(value));
        }
        if ((fieldType.equals(Boolean.class)) || (fieldType.equals(boolean.class))) {
            return Boolean.valueOf(value);
        }
        return value;
    }

    private void buildHeader() {
        headerColumns = new LinkedHashMap<>();
        Row row = rowIterator.next();
        if (row == null) {
            throw new XceliteException("First row in sheet is empty. First row must contain header");
        }
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i, MissingCellPolicy.toPoiMissingCellPolicy(options.getMissingCellPolicy()));
            String cellValue = null;
            if (null != cell) {
                cellValue = cell.getStringCellValue();
                if ((null == cellValue) || (cellValue.isEmpty()))
                    cellValue = null;
            }
            headerColumns.put(i, cellValue);
        };
    }

    private class ColumnsMapper {
        @Getter
        private final Map<String, Col> columnsMap;

        ColumnsMapper(Set<Col> columns) {
            columnsMap = new LinkedHashMap<>();
            for (Col col : columns) {
                columnsMap.put(col.getName(), col);
            }
        }

        Col getColumn(String name) {
            return columnsMap.get(name);
        }
    }

}
