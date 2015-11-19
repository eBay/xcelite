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

import java.io.*;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ebay.xcelite.exceptions.XceliteException;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.sheet.XceliteSheetImpl;

/**
 * Class description...
 * 
 * @author kharel (kharel@ebay.com)
 * @creation_date Nov 9, 2013
 * 
 */
public class Xcelite {

  private final Workbook workbook;
  private File file;

  public Xcelite() {
    workbook = new XSSFWorkbook();
  }

  public Xcelite(File file) {
    try {
      this.file = file;
      workbook = new XSSFWorkbook(new FileInputStream(file));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates new sheet.
   * 
   * @return XceliteSheet object
   */
  public XceliteSheet createSheet() {
    return new XceliteSheetImpl(workbook.createSheet(), file);
  }

  /**
   * Creates new sheet with specified name.
   * 
   * @param name the sheet name   * 
   * @return XceliteSheet object
   */
  public XceliteSheet createSheet(String name) {
    return new XceliteSheetImpl(workbook.createSheet(name), file);
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
    return new XceliteSheetImpl(sheet, file);
  }

  /**
   * Gets the sheet with the specified index.
   * 
   * @param sheetIndex the sheet name
   * @return XceliteSheet object
   */
  public XceliteSheet getSheet(String sheetName) {
    Sheet sheet = workbook.getSheet(sheetName);
    if (sheet == null) {
      throw new XceliteException(String.format("Could not find sheet named \"%s\"", sheetName));
    }
    return new XceliteSheetImpl(sheet, file);
  }

  /**
   * Saves data to the same file given in construction. If no such file
   * specified an exception is thrown.
   */
  public void write() {
    if (file == null) {
      throw new XceliteException("No file given in Xcelite object construction. Consider using method write(file)");
    }
    write(file);
  }

  /**
   * Saves data to a new file.
   * 
   * @param file the file to save the data into
   */
  public void write(File file) {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(file, false);
      workbook.write(out);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      new RuntimeException(e);
    } finally {
      if (out != null)
        try {
          out.close();
        } catch (IOException e) {
          new RuntimeException(e);
        }
    }
  }

  /**
   * write data to the giving OutputStream
   *
   * @param out the OutputStream to write the data into
   */
  public void write(OutputStream out) {
    try {
      workbook.write(out);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      new RuntimeException(e);
    } finally {
      if (out != null)
        try {
          out.close();
        } catch (IOException e) {
          new RuntimeException(e);
        }
    }
  }
  
  /**
   * Gets the excel file as byte array.
   * 
   * @return byte array which represents the excel file
   */
  public byte[] getBytes() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      workbook.write(baos);
    } catch (IOException e) {
      new RuntimeException(e);
    } finally {
      if (baos != null)
        try {
          baos.close();
        } catch (IOException e) {
          new RuntimeException(e);
        }
    }
    return baos.toByteArray();
  }
}
