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

import java.lang.reflect.Field;
import java.util.Date;

import javax.persistence.Entity;

import org.apache.poi.ss.usermodel.Cell;

import com.ebay.xcelite.sheet.XceliteSheet;

/**
 * Class description...
 *
 * @author kharel (kharel@ebay.com)
 * @creation_date Nov 10, 2013
 * 
 */
public abstract class SheetWriterAbs<T> implements SheetWriter<T> {
  
  private static final int MAX_EXCEL_CELL_CARACTERS = 32767;
  protected XceliteSheet sheet;
  protected boolean writeHeader;
  
  public SheetWriterAbs(XceliteSheet sheet, boolean writeHeader) {
    this.sheet = sheet;
    this.writeHeader = writeHeader;
  }
  
  protected void writeToCell(Cell cell, Object fieldValueObj, Class<?> dataType) {
    Class<?> type = fieldValueObj.getClass();
    if (dataType != null) {
      type = dataType;
    }
    if (type == Date.class) {
      cell.setCellValue((Date) fieldValueObj);
    } else if (type == Boolean.class) {
      cell.setCellValue((Boolean) fieldValueObj);
    } else if (type == Double.class || type == double.class || type == Integer.class || type == int.class
        || type == Long.class || type == long.class || type == Float.class || type == float.class
        || type == Short.class || type == short.class) {
      cell.setCellType(Cell.CELL_TYPE_NUMERIC);
      cell.setCellValue(Double.valueOf(fieldValueObj.toString()));
    } else if (type == String.class) {
      cell.setCellType(Cell.CELL_TYPE_STRING);
      String cellValue = extractStringValue(fieldValueObj);
      cell.setCellValue(cellValue);
    } else {
    	Entity entity = type.getAnnotation(javax.persistence.Entity.class);
    	if(entity != null) {
    		try {
    			Field[] fields = type.getDeclaredFields();
    			Field idField = getIdField(fields);
    			Object value = null;
    			// If no Id field
    			if(idField == null && fields.length > 0) {
    				// TODO : Maybe We should sorts field by name and pick the first one. So that the reverse operation can be easy.
    				idField = fields[0]; // Pick a random field
    			}
    			try {
					idField.setAccessible(true);
					value = idField.get(fieldValueObj);
				} catch (IllegalArgumentException e) {
			      throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
			      throw new RuntimeException(e);
				}
    			if(value == null) value = "NOREF";
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(value.toString());
			} catch (SecurityException e) {
		      throw new RuntimeException(e);
			}
    	}
    }
  }

  /**
   * Extract the String value, and shrink it to fix excell string size.
   * @param fieldValueObj
   * @return
   */
  private String extractStringValue(Object fieldValueObj) {
	String cellValue = fieldValueObj.toString();
      if(cellValue == null) cellValue = "";
      if(cellValue.length() > MAX_EXCEL_CELL_CARACTERS) {
    	  cellValue = cellValue.substring(0, 32764);
    	  cellValue += "...";
      }
	return cellValue;
  }

  private Field getIdField(Field[] fields) {
	Field idField = null;
	for (Field field : fields) {
		if(field.getAnnotation(javax.persistence.Id.class) != null) {
			idField = field;
			break;
		}
	}
	return idField;
  }
  
  @Override
  public void generateHeaderRow(boolean generateHeaderRow) {
    this.writeHeader = generateHeaderRow;
  }
  
  @Override
  public XceliteSheet getSheet() {
    return sheet;
  }
}
