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

import com.ebay.xcelite.exceptions.EmptyRowException;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.XceliteSheet;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of the {@link SheetReader} interface that returns the contents
 * of an Excel sheet as a two-dimensional data structure of simple Java objects.
 *
 * By default, a BeanSheetReader copies over the {@link XceliteOptions options} from the
 * {@link com.ebay.xcelite.sheet.XceliteSheet} it is constructed on. This means the
 * options set on the sheet become the default options for the SheetReader, but it can
 * modify option properties locally. However, the user may use the
 * {@link #SimpleSheetReader(XceliteSheet, XceliteOptions)} constructor to
 * use - for one reader only - a completely different set of options.
 *
 * @author kharel (kharel@ebay.com)
 * @since 1.0
 * created Nov 8, 2013
 */
public class SimpleSheetReader extends AbstractSheetReader<Collection<Object>> {

  /**
   * Construct a SimpleSheetReader with custom options
   * @param sheet the {@link XceliteSheet} to read from
   * @param options the {@link XceliteOptions} to configure the reader
   */
  public SimpleSheetReader(XceliteSheet sheet, XceliteOptions options) {
    super(sheet, options);
  }

  /**
   * Construct a SimpleSheetReader with default options
   * @param sheet the {@link XceliteSheet} to read from
   */
  public SimpleSheetReader(XceliteSheet sheet) {
    this(sheet, new XceliteOptions());
  }

  @Override
  public Collection<Collection<Object>> read() {
    List<Collection<Object>> rows = new ArrayList<>();
    Iterator<Row> rowIterator = moveToFirstRow(sheet.getNativeSheet(), options);
    if (!rowIterator.hasNext())
      return rows;

    rowIterator.forEachRemaining(excelRow -> {
      List<Object> row;
      if (isBlankRow(excelRow)) {
        switch (options.getMissingRowPolicy()) {
          case THROW:
            throw new EmptyRowException();
          case RETURN_EMPTY_OBJECT:
            row = new ArrayList<>();
            break;
          case RETURN_NULL:
            row = null;
            break;
          default:
            row = null;
        }
      } else {
        row = fillObject(excelRow);
      }

      if (shouldKeepObject(row, rowPostProcessors)) {
        rows.add(row);
      }
    });
    return rows;
  }

  @SneakyThrows
  private List<Object> fillObject(Row excelRow) {
    List<Object> row = new ArrayList<>();
    Iterator<Cell> cellIterator = excelRow.cellIterator();

    while (cellIterator.hasNext()) {
      Object value = readValueFromCell(cellIterator.next());
      row.add(value);
    }
    return row;
  }
}
