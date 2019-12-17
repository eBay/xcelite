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
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.*;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

/**
 * Main class of the Xcelite package. A Xcelite object is constructed on an Excel
 * file and wraps a POI {@link org.apache.poi.ss.usermodel.Workbook} to allow ORM-like
 * operations on the workbooks' sheets.
 *
 * @author kharel (kharel@ebay.com)
 * @since 1.0
 * created Nov 9, 2013
 */
public class Xcelite {

    private final Workbook workbook;

    //TODO Version 2.0: remove this member variable together with write();
    private File file;

    @Getter
    protected XceliteOptions options;

    /**
     * Create a Xcelite object containing a newly-created POI-workbook
     * and having default {@link XceliteOptions}.
     */
    public Xcelite() {
        workbook = new XSSFWorkbook();
        options = new XceliteOptions();
    }

    /**
     * Create a Xcelite object containing a newly-created POI-workbook
     * and having custom {@link XceliteOptions} via the `options`
     * parameter.
     * @param options Custom options to use on all
     * {@link com.ebay.xcelite.reader.SheetReader} and
     * {@link com.ebay.xcelite.writer.SheetWriter} objects derived from
     * this Xcelite instance
     */
    public Xcelite(XceliteOptions options) {
        this();
        this.options = options;
    }

    /**
     * Create a Xcelite object reading its POI-workbook from an
     * {@link java.io.InputStream}
     * and having default {@link XceliteOptions}.
     * @param inputStream the InputStream to read data from
     */
    public Xcelite(InputStream inputStream) {
        this(inputStream, new XceliteOptions());
    }

    /**
     * Create a Xcelite object reading its POI-workbook from an
     * {@link java.io.InputStream} and having custom {@link XceliteOptions}
     * via the `options` parameter.
     * @param inputStream the InputStream to read data from
     * @param options Custom options to use on all
     * {@link com.ebay.xcelite.reader.SheetReader} and
     * {@link com.ebay.xcelite.writer.SheetWriter} objects derived from
     * this Xcelite instance
     */
    @SneakyThrows
    public Xcelite(InputStream inputStream, XceliteOptions options) {
        if (!inputStream.markSupported()) {
            inputStream = new PushbackInputStream(inputStream, 8);
        }
        workbook = WorkbookFactory.create(inputStream);
        this.options = options;
    }

    /**
     * Create a Xcelite object reading its POI-workbook from an
     * {@link java.io.File}
     * and having default {@link XceliteOptions}.
     * @param file the File to read data from
     */
    @SneakyThrows
    public Xcelite(File file) {
        this.file = file;
        workbook = new XSSFWorkbook(new FileInputStream(file));
    }

    /**
     * Creates a new {@link com.ebay.xcelite.sheet.XceliteSheet} without a name.
     *
     * @return XceliteSheet newly created {@link com.ebay.xcelite.sheet.XceliteSheet}
     */
    public XceliteSheet createSheet() {
        return new XceliteSheetImpl(workbook.createSheet(), options);
    }

    /**
     * Creates a new {@link com.ebay.xcelite.sheet.XceliteSheet} with specified name.
     *
     * @param name the sheet name
     * @return XceliteSheet newly created {@link com.ebay.xcelite.sheet.XceliteSheet}
     */
    public XceliteSheet createSheet(String name) {
        return new XceliteSheetImpl(workbook.createSheet(name), options);
    }

    /**
     * Gets the {@link com.ebay.xcelite.sheet.XceliteSheet} at
     * the specified index and the {@link XceliteOptions options} from
     * this Xcelite instance.
     *
     * @param sheetIndex the sheet index
     * @return XceliteSheet object
     */
    public XceliteSheet getSheet(int sheetIndex) {
        return ofNullable(workbook.getSheetAt(sheetIndex))
                .map(s -> new XceliteSheetImpl(s, options))
                .orElseThrow(() -> new XceliteException(String.format("Could not find sheet at index %s", sheetIndex)));
    }

    /**
     * Gets the {@link com.ebay.xcelite.sheet.XceliteSheet} with
     * the specified name and the {@link XceliteOptions options} from
     * this Xcelite instance.
     *
     * @param sheetName the sheet name
     * @return XceliteSheet object
     */
    public XceliteSheet getSheet(String sheetName) {
        return ofNullable(workbook.getSheet(sheetName))
                .map(s -> new XceliteSheetImpl(s, options))
                .orElseThrow(() -> new XceliteException(String.format("Could not find sheet named \"%s\"", sheetName)));
    }

    /**
     * Returns all sheets.
     *
     * @return the list of sheets (a list of {@link XceliteSheet} objects)
     * or throws a {@link XceliteException} if no sheets exist
     */
    // TODO for version 2.0, remove the exception, return empty list
    public List<XceliteSheet> getSheets() {
        if (workbook.getNumberOfSheets() == 0) {
            throw new XceliteException("Could not find any sheet");
        }

        List<XceliteSheet> xceliteSheets = new ArrayList<>();
        workbook.sheetIterator().forEachRemaining(
                sheet -> xceliteSheets.add(new XceliteSheetImpl(sheet)));

        return xceliteSheets;
    }

    /**
     * Saves data to the input file.
     *
     * @deprecated since 1.2. When reading the Workbook from an {@link java.io.InputStream},
     * eg. from a web service, member `file` will be null, causing an Exception.
     * Use the explicit methods to write to a {@link java.io.File} or {@link java.io.OutputStream}
     */
    @SneakyThrows
    @Deprecated
    public void write() {
        write(file);
    }

    /**
     * Saves data to a {@link java.io.File}.
     *
     * @param file the {@link java.io.File} to save the data into
     */
    @SneakyThrows
    public void write(File file) {
        try (FileOutputStream out = new FileOutputStream(file, false)) {
            write(out);
        }
    }

    /**
     * Saves data to a new {@link java.io.OutputStream}.
     *
     * @param out the {@link java.io.OutputStream} to save the data into
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

    public void setOptions(XceliteOptions options) {
        this.options = new XceliteOptions(options);
    }
}
