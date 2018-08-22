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
package com.ebay.xcelite.column;

import com.ebay.xcelite.annotate.NoConverterClass;
import com.ebay.xcelite.annotations.AnyColumn;
import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;
import com.ebay.xcelite.exceptions.ColumnNotFoundException;
import com.ebay.xcelite.exceptions.XceliteException;
import lombok.Getter;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

public class ColumnsExtractor {

    @Getter
    private LinkedHashSet<Col> columns;

    @Getter
    private Col anyColumn;
    private final Class<?> type;
    private LinkedHashSet<Col> colsOrdering;

    public ColumnsExtractor(Class<?> type) {
        this.type = type;
        columns = new LinkedHashSet<>();
        columnsOrdering();
    }

    private void columnsOrdering() {
        Row rowAnnotation = type.getAnnotation(Row.class);
        if (rowAnnotation == null || rowAnnotation.colsOrder().length == 0) {
            return;
        }
        colsOrdering = new LinkedHashSet<>();
        for (String column : rowAnnotation.colsOrder()) {
            colsOrdering.add(new Col(column));
        }
    }

    @SuppressWarnings("unchecked")
    public void extract() {
        getAllFields(type, withAnnotation(Column.class))
                .forEach(columnField -> {
                    Column annotation = columnField.getAnnotation(Column.class);
                    Col col = of(annotation)
                            .filter(column -> !column.name().isEmpty())
                            .map(column -> new Col(column.name(), columnField.getName()))
                            .orElse(new Col(columnField.getName(), columnField.getName()));

                    if (annotation.ignoreType()) {
                        col.setType(String.class);
                    } else {
                        col.setType(columnField.getType());
                    }
                    if (!annotation.dataFormat().isEmpty()) {
                        col.setDataFormat(annotation.dataFormat());
                    }
                    if (annotation.converter() != NoConverterClass.class) {
                        col.setConverter(annotation.converter());
                    }
                    columns.add(col);
                });

        if (colsOrdering != null) {
            orderColumns();
        }

        anyColumn = extractAnyColumn(type);
    }

    @SuppressWarnings("unchecked")
    private static Col extractAnyColumn(final Class<?> type) {
        Col anyColumn = null;
        Set<Field> anyColumnFields = getAllFields(type, withAnnotation(AnyColumn.class));

        if (anyColumnFields.size() == 1) {
            Field anyColumnField = anyColumnFields.iterator().next();
            if (!anyColumnField.getType().isAssignableFrom(Map.class)) {
                throw new XceliteException(
                        String.format("AnyColumn field \"%s\" should be of type Map.class or assignable from Map.class",
                                anyColumnField.getName()));
            }
            anyColumn = new Col(anyColumnField.getName(), anyColumnField.getName());
            anyColumn.setAnyColumn(true);
            AnyColumn annotation = anyColumnField.getAnnotation(AnyColumn.class);
            anyColumn.setType(annotation.as());
            if (annotation.converter() != NoConverterClass.class) {
                anyColumn.setConverter(annotation.converter());
            }
        } else if (anyColumnFields.size() > 1) {
            throw new XceliteException("Multiple @AnyColumn fields are not allowed");
        }
        return anyColumn;
    }

    private void orderColumns() {
        // build temporary columns map and then use it to fill fieldName in colsOrdering set
        Map<String, Col> map = columns.stream().collect(Collectors.toMap(Col::getName, col -> col));

        colsOrdering.forEach(col -> {
            if (columns.contains(col)) {
                Col column = map.get(col.getName());
                column.copyTo(col);
            } else {
                throw new ColumnNotFoundException(col.getName());
            }
        });

        if (colsOrdering.size() != columns.size()) {
            throw new XceliteException("Not all columns are specified in Row columns ordering");
        }
        columns = colsOrdering;
    }
}
