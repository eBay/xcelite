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

import java.util.Collection;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.styles.CellStylesBank;

/**
 * Class description...
 *
 * @author kharel (kharel@ebay.com)
 * created Nov 10, 2013
 * 
 */
public class SimpleSheetWriter extends SheetWriterAbs<Collection<Object>> {
  
  public SimpleSheetWriter(XceliteSheet sheet) {
    super(sheet, false);
  }

  @Override
  public void write(Collection<Collection<Object>> data) {
    int i = 0;
    for (Collection<Object> row : data) {
      Row excelRow = sheet.getNativeSheet().createRow(i);
      int j = 0;
      for (Object column : row) {
        Cell cell = excelRow.createCell(j);
        if (writeHeader && i == 0) {
          cell.setCellStyle(CellStylesBank.get(sheet.getNativeSheet().getWorkbook()).getBoldStyle());
        }
        writeToCell(cell, column, null);        
        ++j;
      }
      ++i;
    }
  }  
}
