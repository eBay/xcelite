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

import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.styles.CellStylesBank;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class description...
 *
 * @author kharel (kharel@ebay.com)
 * created Nov 10, 2013
 */
public class SimpleSheetWriter extends AbstractSheetWriter<Collection<Object>> {
    private CellStyle boldStyle;
    public SimpleSheetWriter(XceliteSheet sheet) {
        super(sheet, false);
        boldStyle = CellStylesBank.get(sheet.getNativeSheet().getWorkbook()).getBoldStyle();
    }

    @Override
    public void writeRow(final Collection<Object> row, final Row excelRow, final int rowIndex) {
        final AtomicInteger j = new AtomicInteger(0);
        row.forEach(column -> {
            Cell cell = excelRow.createCell(j.intValue());
            if (writeHeader && rowIndex == 0) {
                cell.setCellStyle(boldStyle);
            }
            writeToCell(cell, column, null);
            j.incrementAndGet();
        });
    }
}
