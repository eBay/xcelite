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

import static org.reflections.ReflectionUtils.withAnnotation;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.reflections.ReflectionUtils;

import com.ebay.xcelite.annotate.NoConverterClass;
import com.ebay.xcelite.annotations.AnyColumn;
import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;
import com.ebay.xcelite.exceptions.XceliteException;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


public class ColumnsExtractor {

  private final Class<?> type;
  private Set<Col> columns;
  private Col anyColumn;
  private Set<Col> colsOrdering;

  public ColumnsExtractor(Class<?> type) {
    this.type = type;
    columns = Sets.newLinkedHashSet();
    columnsOrdering();
  }

  private void columnsOrdering() {
    Row rowAnnotation = type.getAnnotation(Row.class);
    if (rowAnnotation == null || rowAnnotation.colsOrder() == null || rowAnnotation.colsOrder().length == 0) return;
    colsOrdering = Sets.newLinkedHashSet();
    for (String column : rowAnnotation.colsOrder()) {
      colsOrdering.add(new Col(column));
    }
  }

  @SuppressWarnings("unchecked")
  public void extract() {    
    Set<Field> columnFields = ReflectionUtils.getAllFields(type, withAnnotation(Column.class));
    for (Field columnField : columnFields) {
      Column annotation = columnField.getAnnotation(Column.class);
      Col col = null;
      if (annotation.name().isEmpty()) {
        col = new Col(columnField.getName(), columnField.getName());        
      } else {
        col = new Col(annotation.name(), columnField.getName());        
      }      
      
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
    }
    
    if (colsOrdering != null) {
      orderColumns();
    }
    
    extractAnyColumn();
  }
  

  @SuppressWarnings("unchecked")
  public void extractJpa() {    
    Set<Field> idFields = ReflectionUtils.getAllFields(type, withAnnotation(javax.persistence.Id.class));
    Set<Field> columnFields = ReflectionUtils.getAllFields(type, withAnnotation(javax.persistence.Column.class));
    Set<Field> manyToOneFields = ReflectionUtils.getAllFields(type, withAnnotation(javax.persistence.ManyToOne.class));

    for (Field columnField : idFields) {
      javax.persistence.Id annotation = columnField.getAnnotation(javax.persistence.Id.class);
      if(annotation == null) continue;
      Col col = null;
      col = new Col(columnField.getName(), columnField.getName(), columnField.getType());
      columns.add(col);
    }
    
    for (Field columnField : columnFields) {
      javax.persistence.Column annotation = columnField.getAnnotation(javax.persistence.Column.class);
      if(annotation == null) continue;
      Col col = null;
      if (annotation.name().isEmpty()) {
        col = new Col(columnField.getName(), columnField.getName(),  columnField.getType());        
      } else {
        col = new Col(annotation.name(), columnField.getName(), columnField.getType());
      }
      columns.add(col);
    }

    for (Field columnField : manyToOneFields) {
      ManyToOne annotation = columnField.getAnnotation(javax.persistence.ManyToOne.class);
      Col col = null;
      if(annotation == null) continue;
      JoinColumn joinColumn = columnField.getAnnotation(javax.persistence.JoinColumn.class);
      if (joinColumn.name().isEmpty()) {
        col = new Col(columnField.getName(), columnField.getName(), columnField.getType());        
      } else {
        col = new Col(joinColumn.name(), columnField.getName(), columnField.getType());
      }
      columns.add(col);
    }
  }
  
  
  @SuppressWarnings("unchecked")
  private void extractAnyColumn() {
    Set<Field> anyColumnFields = ReflectionUtils.getAllFields(type, withAnnotation(AnyColumn.class));    
    if (anyColumnFields.size() > 0) {
      if (anyColumnFields.size() > 1) {
        throw new XceliteException("Multiple AnyColumn fields are not allowed");
      }
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
    }
  }

  private void orderColumns() {
    // build temporary columns map and then use it to fill fieldName in colsOrdering set
    Map<String, Col> map = Maps.newHashMap();
    for (Col col : columns) {
      map.put(col.getName(), col);
    }
    
    for (Col col : colsOrdering) {
      if (columns.contains(col)) {
        Col column = map.get(col.getName());        
        column.copyTo(col);
      } else {
        throw new RuntimeException(String.format("Unrecognized column \"%s\" in Row columns ordering", col.getName()));
      }
    }
    
    if (colsOrdering.size() != columns.size()) {
      throw new RuntimeException(String.format("Not all columns are specified in Row columns ordering"));
    }
    columns = colsOrdering;
  }

  public LinkedHashSet<Col> getColumns() {
    return (LinkedHashSet<Col>) columns;
  }

  public Col getAnyColumn() {
    return anyColumn;
  }
}
