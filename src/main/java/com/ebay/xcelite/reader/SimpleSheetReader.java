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

import com.ebay.xcelite.sheet.XceliteSheet;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.options.XceliteOptionsImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.ebay.xcelite.sheet.XceliteSheet;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Implementation of the {@link SheetReader} interface that returns the contents
 * of an Excel sheet as a two-dimensional data structure.
 * 
 * @author kharel (kharel@ebay.com)
 * created Nov 8, 2013
 */
public class SimpleSheetReader extends AbstractSheetReader<Collection<Object>> {

  public SimpleSheetReader(XceliteSheet sheet, XceliteOptions options) {
    super(sheet, options);
  }

  /**
   * Construct a SimpleSheetReader with default options
   * @param sheet the {@link XceliteSheet} to read from
   */
  public SimpleSheetReader(XceliteSheet sheet) {
    this(sheet, new XceliteOptionsImpl());
  }

  @Override
  public Collection<Collection<Object>> read() {
    List<Collection<Object>> rows = new ArrayList<>();
    Iterator<Row> rowIterator = moveToFirstRow(sheet.getNativeSheet(), options);
    if (null == rowIterator) return rows;

    while (rowIterator.hasNext()) {
      Row excelRow = rowIterator.next();
      if (options.isSkipBlankRows() && isBlankRow(excelRow))
        continue;

      List<Object> row = Lists.newArrayList();
      Iterator<Cell> cellIterator = excelRow.cellIterator();

      while (cellIterator.hasNext()) {
        Object value = readValueFromCell(cellIterator.next());
        row.add(value);
      }

      boolean keepRow = true;
      for (RowPostProcessor<Collection<Object>> rowPostProcessor : rowPostProcessors) {
        keepRow = rowPostProcessor.process(row);
        if (!keepRow) break;
      }
      if (keepRow) {
        rows.add(row);
      }
    }
    return rows;
  }
}
