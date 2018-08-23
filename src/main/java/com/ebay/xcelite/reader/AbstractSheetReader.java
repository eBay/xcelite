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

import java.util.Iterator;
import java.util.List;

import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.options.XceliteOptionsImpl;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;

import com.ebay.xcelite.sheet.XceliteSheet;
import com.google.common.collect.Lists;
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

  protected final List<RowPostProcessor<T>> rowPostProcessors;

  @Getter
  protected final XceliteOptions options;


  public AbstractSheetReader(XceliteSheet sheet) {
    this (sheet, new XceliteOptionsImpl());
  }

  public AbstractSheetReader(XceliteSheet sheet, XceliteOptions options) {
    this.sheet = sheet;
    this.options = options;
    rowPostProcessors = Lists.newArrayList();
  }

  /**
   * @deprecated since 1.2 use the constructor using {@link XceliteOptions}
   * and set {@link XceliteOptions#setSkipRowsBeforeColumnDefinitionRow(Integer) setSkipLinesBeforeHeader}
   * to 1
   * @param sheet
   * @param skipHeaderRow
   */
  @Deprecated
  public AbstractSheetReader(XceliteSheet sheet, boolean skipHeaderRow) {
    this.sheet = sheet;
    this.options = new XceliteOptionsImpl();
    if (skipHeaderRow)
      options.setSkipRowsBeforeColumnDefinitionRow(1);
    rowPostProcessors = Lists.newArrayList();
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
          case STRING:
            cellValue = cell.getStringCellValue();
            break;
          case NUMERIC:
            cellValue = cell.getNumericCellValue();
            break;
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

  static Iterator<Row> moveToFirstRow(Sheet nativeSheet, XceliteOptions options) {
    Iterator<Row> rowIterator = nativeSheet.iterator();
    if (!rowIterator.hasNext())
      return null;
    rowIterator.next();
    if (options.getSkipRowsBeforeColumnDefinitionRow() > 0) {
      int rowsToSkip = options.getSkipRowsBeforeColumnDefinitionRow();
      while ((rowsToSkip > 0) && (rowIterator.hasNext())) {
        rowIterator.next();
        rowsToSkip--;
      }
      if (rowsToSkip > 0)
        throw new IllegalArgumentException("SkipLinesBeforeHeader cannot be bigger than length of sheet");
    }
    return rowIterator;
  }
}
