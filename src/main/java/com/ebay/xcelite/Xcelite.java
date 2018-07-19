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
package com.ebay.xcelite;

import com.ebay.xcelite.exceptions.XceliteException;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.sheet.XceliteSheetImpl;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

import static java.util.Optional.ofNullable;

/**
 * Class description...
 *
 * @author kharel (kharel@ebay.com)
 * created Nov 9, 2013
 */
public class Xcelite {

    private final Workbook workbook;
    private File file;

    public Xcelite() {
        workbook = new XSSFWorkbook();
    }

    @SneakyThrows
    public Xcelite(InputStream inputStream) {
        workbook = new XSSFWorkbook(inputStream);
    }

    @SneakyThrows
    public Xcelite(File file) {
        this.file = file;
        workbook = new XSSFWorkbook(new FileInputStream(file));
    }

    /**
     * Creates new sheet.
     *
     * @return XceliteSheet object
     */
    public XceliteSheet createSheet() {
        return new XceliteSheetImpl(workbook.createSheet());
    }

    /**
     * Creates new sheet with specified name.
     *
     * @param name the sheet name   *
     * @return XceliteSheet object
     */
    public XceliteSheet createSheet(String name) {
        return new XceliteSheetImpl(workbook.createSheet(name));
    }

    /**
     * Gets the sheet at the specified index.
     *
     * @param sheetIndex the sheet index
     * @return XceliteSheet object
     */
    public XceliteSheet getSheet(int sheetIndex) {
        return ofNullable(workbook.getSheetAt(sheetIndex))
                .map(XceliteSheetImpl::new)
                .orElseThrow(() -> new XceliteException(String.format("Could not find sheet at index %s", sheetIndex)));
    }

    /**
     * Gets the sheet with the specified name.
     *
     * @param sheetName the sheet name
     * @return XceliteSheet object
     */
    public XceliteSheet getSheet(String sheetName) {
        return ofNullable(workbook.getSheet(sheetName))
                .map(XceliteSheetImpl::new)
                .orElseThrow(() -> new XceliteException(String.format("Could not find sheet named \"%s\"", sheetName)));
    }

    /**
     * Gets all sheets.
     *
     * @return the list of sheets (a list of {@link XceliteSheet} objects.) or XceliteException
     */
    public List<XceliteSheet> getSheets() {
        if (workbook.getNumberOfSheets() == 0) {
            throw new XceliteException("Could not find any sheet");
        }

        List<XceliteSheet> xceliteSheets = Lists.newArrayList();

        workbook.sheetIterator()
                .forEachRemaining(sheet -> xceliteSheets.add(new XceliteSheetImpl(sheet)));

        return xceliteSheets;
    }

    /**
     * Saves data to a new file.
     *
     * @param file the file to save the data into
     */
    @SneakyThrows
    public void write(File file) {
        try (FileOutputStream out = new FileOutputStream(file, false)) {
            write(out);
        }
    }

    /**
     * Saves data to a new outputStream.
     *
     * @param out the outputstream to save the data into
     */
    @SneakyThrows
    public void write(OutputStream out) {
        workbook.write(out);
    }

    /**
     * Gets the excel file as byte array.
     *
     * @return byte array which represents the excel file
     */
    @SneakyThrows
    public byte[] getBytes() {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
