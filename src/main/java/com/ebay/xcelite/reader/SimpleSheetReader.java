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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Class description...
 *
 * @author kharel (kharel@ebay.com)
 * created Nov 8, 2013
 */
public class SimpleSheetReader extends SheetReaderAbs<Collection<Object>> {

    public SimpleSheetReader(XceliteSheet sheet) {
        super(sheet, false);
    }

    @Override
    public Collection<Collection<Object>> read() {
        List<Collection<Object>> rows = Lists.newArrayList();
        Iterator<Row> rowIterator = sheet.getNativeSheet().iterator();
        boolean firstRow = true;
        while (rowIterator.hasNext()) {
            Row excelRow = rowIterator.next();
            if (firstRow && skipHeader) {
                firstRow = false;
                continue;
            }
            List<Object> row = Lists.newArrayList();
            Iterator<Cell> cellIterator = excelRow.cellIterator();
            boolean blankRow = true;
            while (cellIterator.hasNext()) {
                Object value = readValueFromCell(cellIterator.next());
                if (blankRow && value != null && !String.valueOf(value).isEmpty()) {
                    blankRow = false;
                }
                row.add(value);
            }
            if (blankRow) continue;
            boolean keepRow = true;
            for (RowPostProcessor<Collection<Object>> rowPostProcessor: rowPostProcessors) {
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
