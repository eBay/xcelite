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
import com.ebay.xcelite.policies.TrailingEmptyRowPolicy;
import com.ebay.xcelite.sheet.XceliteSheet;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ebay.xcelite.policies.MissingRowPolicy.SKIP;
import static org.reflections.ReflectionUtils.withName;

/**
 * Implementation of the {@link SheetReader} interface that returns the contents
 * of an Excel sheet as a {@link java.util.Collection}. Each entry into that Collection
 * represents one row in the Excel sheet.
 *
 * By default, a BeanSheetReader copies over the {@link XceliteOptions options} from the
 * {@link com.ebay.xcelite.sheet.XceliteSheet} it is constructed on. This means the
 * options set on the sheet become the default options for the SheetReader, but it can
 * modify option properties locally. However, the user may use the
 * {@link #BeanSheetReader(XceliteSheet, XceliteOptions, Class)} constructor to
 * use - for one reader only - a completely different set of options.
 *
 * @author kharel (kharel@ebay.com)
 * @since 1.0
 * created Sep 9, 2013
 */
public class BeanSheetReader<T> extends AbstractSheetReader<T> {
    private final Col anyColumn;
    private final ColumnsMapper mapper;
    private final Class<T> type;
    private Map<Integer, String> headerColumns;
    private Iterator<Row> rowIterator;
    public boolean expectsHeaderRow(){return true;}

    /**
     * Construct a BeanSheetReader with custom options. The Reader will create
     * a copy of the options object, therefore later changes of this object will not
     * influence the behavior of this reader
     *
     * @param sheet the {@link XceliteSheet} to read from
     * @param options the {@link XceliteOptions} to configure the reader
     * @param type class of the beans
     */
    public BeanSheetReader(XceliteSheet sheet, XceliteOptions options, Class<T> type) {
        super(sheet, options);
        this.type = type;
        ColumnsExtractor extractor = new ColumnsExtractor(type);
        extractor.extract();
        anyColumn = extractor.getAnyColumn();
        LinkedHashSet<Col> declaredColumns = extractor.getColumns();
        mapper = new ColumnsMapper(declaredColumns);
    }

    /**
     * Construct a BeanSheetReader with options from the {@link XceliteSheet}
     * @param sheet the {@link XceliteSheet} to read from
     * @param type class of the beans
     */
    public BeanSheetReader(XceliteSheet sheet, Class<T> type) {
        this(sheet, sheet.getOptions(), type);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SneakyThrows
    public Collection<T> read() {
        List<T> data = new ArrayList<>();
        final AtomicInteger lastNonEmptyRowId = new AtomicInteger();

        rowIterator = sheet.moveToHeaderRow(options.getHeaderRowIndex(), false);
        if (!rowIterator.hasNext())
            return data;

        buildHeader();
        validateColumns();
        rowIterator = sheet.moveToFirstDataRow(this, false);

        rowIterator.forEachRemaining(excelRow -> {
            T object;

            if (isBlankRow(excelRow)) {
                switch (options.getMissingRowPolicy()) {
                    case THROW:
                        throw new EmptyRowException();
                    case EMPTY_OBJECT:
                        object = fillObject(excelRow);
                        break;
                    case NULL:
                        object = null;
                        break;
                    default:
                        object = null;
                }
                if (!options.getMissingRowPolicy().equals(SKIP)) {
                    if (shouldKeepObject(object, rowPostProcessors)) {
                        data.add(object);
                    }
                }
            } else {
                object = fillObject(excelRow);
                if (shouldKeepObject(object, rowPostProcessors)) {
                    data.add(object);
                }
                lastNonEmptyRowId.set(data.size());
            }
        });

        return applyTrailingEmptyRowPolicy(data, lastNonEmptyRowId.intValue());
    }

    @SneakyThrows
    private T fillObject(Row row) {
        T object = getNewObject();

        for (int i = 0; i < headerColumns.keySet().size(); i++) {
            String columnName = headerColumns.get(i);
            checkHasThrowPolicyMustThrow(row, i);
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

    @SneakyThrows
    T getNewObject(){
        return type.newInstance();
    }

    /**
     * check that every @column is found in header
     */
    private void validateColumns() {
        if (anyColumn != null) {
            return;
        }
        Collection<String> declaredHeaders = mapper.getDeclaredHeaderNames();
        Collection<String> headers = headerColumns.values();
        if (!options.isHeaderParsingIsCaseSensitive()) {
            headers = headers.stream().map(n -> {
                n = (n == null)? null: n.toLowerCase();
                return n;}
            ).collect(Collectors.toList());
            declaredHeaders =   declaredHeaders.stream().map(n -> {
                n = (n == null)? null: n.toLowerCase();
                return n;}
            ).collect(Collectors.toList());
        }

        if (!headers.containsAll(declaredHeaders)) {
            throw new ColumnNotFoundException(declaredHeaders.iterator().next());
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
        } else if ((fieldType.equals(Integer.class)) || (fieldType.equals(int.class))) {
            return Double.valueOf(value).intValue();
        } else if ((fieldType.equals(Short.class)) || (fieldType.equals(short.class))) {
            return Double.valueOf(value).shortValue();
        } else if ((fieldType.equals(Long.class)) || (fieldType.equals(long.class))) {
            return Double.valueOf(value).longValue();
        } else if ((fieldType.equals(Float.class)) || (fieldType.equals(float.class))) {
            return Double.valueOf(value).floatValue();
        } else if ((fieldType.equals(Character.class)) || (fieldType.equals(char.class))) {
            return value.charAt(0);
        } else if (fieldType.equals(Date.class)) {
            return DateUtil.getJavaDate(Double.valueOf(value));
        } else if ((fieldType.equals(Boolean.class)) || (fieldType.equals(boolean.class))) {
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
            if (!checkHasThrowPolicyMustThrow(row, i)) {
                Cell cell = row.getCell(i, MissingCellPolicy.toPoiMissingCellPolicy(options.getMissingCellPolicy()));
                String cellValue = null;
                if (null != cell) {
                    cellValue = cell.getStringCellValue();
                    if ((null == cellValue) || (cellValue.isEmpty()))
                        cellValue = null;
                }
                headerColumns.put(i, cellValue);
            }
        }
    }

    private boolean checkHasThrowPolicyMustThrow(Row row, int colIdx) {
        MissingCellPolicy policy = options.getMissingCellPolicy();
        if (policy.equals(MissingCellPolicy.THROW)) {
            Cell cell = row.getCell(colIdx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (null == cell) {
                String colName = headerColumns.get(colIdx);
                if (null != colName)
                    throw new EmptyCellException(colName);
                else
                    throw new EmptyCellException();
            }
            return true;
        }
        return false;
    }

    private class ColumnsMapper {
        private final Map<String, Col> columnsMap;
        private final Map<String, Col> lowerCaseColumnsMap;

        ColumnsMapper(Set<Col> columns) {
            columnsMap = new LinkedHashMap<>();
            lowerCaseColumnsMap = new LinkedHashMap<>();
            for (Col col : columns) {
                columnsMap.put(col.getName(), col);
                lowerCaseColumnsMap.put(col.getName().toLowerCase(), col);
            }
        }

        Col getColumn(String name) {
            if (options.isHeaderParsingIsCaseSensitive() || (null == name)) {
                return columnsMap.get(name);
            } else {
                return lowerCaseColumnsMap.get(name.toLowerCase());
            }
        }

        Set<String> getDeclaredHeaderNames() {
            Set<String> declaredHeaders = columnsMap
                    .values()
                    .stream()
                    .map(c -> c.getName())
                    .collect(Collectors.toSet());
            return declaredHeaders;
        }
    }

}
