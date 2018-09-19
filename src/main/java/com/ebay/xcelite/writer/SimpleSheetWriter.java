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

import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.styles.CellStylesBank;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * An concrete implementation of the {@link SheetWriter} interface that writes
 * 2-dimensional collections of simple, unannotated Java beans to Excel sheets.
 * Each entry in the outer collection represents one row in the spreadsheet
 * while each entry in the inner collection represents one cell (column) in the row.
 *
 * This writer class does not write a header row, as no column names are defined.
 *
 * Preferably, this class should not directly be instantiated, but you should
 * call {@link XceliteSheet#getSimpleWriter()}
 *
 * By default, a SimpleSheetWriter copies over the {@link XceliteOptions options} from the
 * {@link com.ebay.xcelite.sheet.XceliteSheet} it is constructed on. This means the
 * options set on the sheet become the default options for the SheetWriter, but it can
 * modify option properties locally. However, the user may use the
 * {@link #SimpleSheetWriter(XceliteSheet, XceliteOptions)} constructor to
 * use - for one writer only - a completely different set of options.

 *
 * @author kharel (kharel@ebay.com)
 * @since 1.0
 * created Nov 10, 2013
 */
public class SimpleSheetWriter extends AbstractSheetWriter<Collection<Object>> {
    CellStyle boldStyle = CellStylesBank.get(sheet.getNativeSheet().getWorkbook()).getBoldStyle();

    //TODO version 2.x remove if possible
    public SimpleSheetWriter(XceliteSheet sheet) {
        super(sheet);
        sheet.getOptions().setGenerateHeaderRow(false);
    }

    /**
     * Construct a {@link SimpleSheetWriter} on the given {@link XceliteSheet sheet} using
     * the given {@link XceliteOptions options}. Values from the options parameter
     * are copied over, later changes to the options object will not affect the
     * options of this writer.
     * @param sheet the sheet to construct the SimpleSheetWriter on.
     * @param options options for this SimpleSheetWriter.
     */
    public SimpleSheetWriter(XceliteSheet sheet, XceliteOptions options) {
        super(sheet, options);
    }

    @Override
    public void write(Collection<Collection<Object>> data) {
        CellStyle boldStyle = CellStylesBank.get(sheet.getNativeSheet().getWorkbook()).getBoldStyle();
        final AtomicInteger i = new AtomicInteger(0);

        data.forEach(row -> {
            Row excelRow = sheet.getNativeSheet().createRow(i.intValue());
            final AtomicInteger j = new AtomicInteger(0);
            row.forEach(column -> {
                Cell cell = excelRow.createCell(j.intValue());
                if (options.isGenerateHeaderRow() && i.intValue() == 0) {
                    cell.setCellStyle(boldStyle);
                }
                writeToCell(cell, column, null);
                j.incrementAndGet();
            });
            i.incrementAndGet();
        });
    }

    /**
     * Takes an object collection and writes it to the
     * {@link XceliteSheet} object this writer is operating on.
     *
     * @param data Object collection representing one row
     * @param excelRow the row object in the spreadsheet to write to
     * @param rowIndex row index of the row object in the spreadsheet to write to
     * @since 1.0
     */
    @Override
    public void writeRow(Collection<Object> data, Row excelRow, int rowIndex) {
        final AtomicInteger j = new AtomicInteger(0);
        data.forEach(column -> {
            Cell cell = excelRow.createCell(j.intValue());
            if (options.isGenerateHeaderRow() && rowIndex == 0) {
                cell.setCellStyle(boldStyle);
            }
            writeToCell(cell, column, null);
            j.incrementAndGet();
        });
    }

    /*
    No-Op for SimpleSheetWriter
     */
    void writeHeader() {}
}
