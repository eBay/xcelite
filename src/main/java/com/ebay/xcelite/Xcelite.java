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
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

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
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        if (sheet == null) {
            throw new XceliteException(String.format("Could not find sheet at index %s", sheetIndex));
        }
        return new XceliteSheetImpl(sheet);
    }

    /**
     * Gets the sheet with the specified name.
     *
     * @param sheetName the sheet name
     * @return XceliteSheet object
     */
    public XceliteSheet getSheet(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new XceliteException(String.format("Could not find sheet named \"%s\"", sheetName));
        }
        return new XceliteSheetImpl(sheet);
    }

    /**
     * Saves data to the input file.
     */
    @SneakyThrows
    @Deprecated
    public void write() {
        write(file);
    }

    /**
     * Saves data to a new file.
     *
     * @param file the file to save the data into
     */
    @SneakyThrows
    public void write(File file) {
        FileOutputStream out = new FileOutputStream(file, false);
        write(out);
        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        write(baos);
        try {
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return baos.toByteArray();
    }
}
