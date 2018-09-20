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
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.Collection;
import java.util.Date;

/**
 * An abstract implementation of {@link SheetWriter} writer classes that can serialize
 * annotated Java objects to Excel workbooks.
 * Extended by {@link BeanSheetWriter} which writes Java beans object collections
 * and {@link SimpleSheetWriter} which writes collections of un-annotated cell objects.
 *
 * Concrete implementations must override the {@link SheetWriter#write(Collection)} method.
 *
 * By default, a SheetWriter copies over the {@link XceliteOptions options} from the sheet
 * it is constructed on. By this, the {@link com.ebay.xcelite.sheet.XceliteSheet} become the
 * default options, but the SheetWriter can modify option properties locally. However, the user
 * may use the {@link #AbstractSheetWriter(XceliteSheet, XceliteOptions)} constructor to
 * use - for one writer only - a completely different set of options from the sheet options.
 *
 * @author kharel (kharel@ebay.com)
 * @since 1.0
 * created Nov 10, 2013
 */
@Getter
public abstract class AbstractSheetWriter<T> implements SheetWriter<T> {
    protected XceliteSheet sheet;

    protected XceliteOptions options;

    /**
     * Construct a {@link SheetWriter} on the given {@link XceliteSheet sheet} using
     * the given {@link XceliteOptions options}. Values from the options parameter
     * are copied over, later changes to the options object will not affect the
     * options of this writer.
     * @param sheet the sheet to construct the SheetWriter on.
     * @param options options for this SheetWriter.
     */
    AbstractSheetWriter(XceliteSheet sheet, XceliteOptions options) {
        this.sheet = sheet;
        this.options = new XceliteOptions(options);
    }

    /**
     * Construct a {@link SheetWriter} on the given {@link XceliteSheet sheet} using
     * {@link XceliteOptions options} from the sheet. Sheet options are copied
     * over, later changes will not affect the options of this writer.
     * @param sheet the sheet to construct the SheetWriter on.
     */
    //TODO version 2.x remove if possible
    AbstractSheetWriter(XceliteSheet sheet) {
        this (sheet, sheet.getOptions());
    }

    /**
     * @deprecated since 1.2 use the constructor using {@link XceliteOptions}
     * and set {@link XceliteOptions#setGenerateHeaderRow(boolean)}
     * to true
     * @param sheet The sheet to write to
     * @param writeHeader whether or not one header row should be written
     */
    @Deprecated
    AbstractSheetWriter(XceliteSheet sheet, boolean writeHeader) {
        this(sheet);
        options.setGenerateHeaderRow(writeHeader);
    }

    void writeToCell(Cell cell, Object fieldValueObj, Class<?> dataType) {
        if (null == fieldValueObj) {
            cell.setCellType(CellType.BLANK);
            return;
        }
        Class<?> type = fieldValueObj.getClass();
        if (dataType != null) {
            type = dataType;
        }
        if (type.equals(Date.class)) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue((Date) fieldValueObj);
        } else if ((type.equals(Boolean.class))
                || (type.equals(boolean.class))){
            cell.setCellType(CellType.BOOLEAN);
            cell.setCellValue((Boolean) fieldValueObj);
        } else if ((Number.class.isAssignableFrom(type))
                || (fieldValueObj instanceof Number))  {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(((Number) fieldValueObj).doubleValue());
        } else {
            cell.setCellType(CellType.STRING);
            cell.setCellValue(fieldValueObj.toString());
        }
    }

    /**
     * @deprecated since 1.2. Use {@link #setGenerateHeaderRow(boolean) instead}
     */
    @Deprecated
    @Override
    public void generateHeaderRow(boolean generateHeaderRow) {
        options.setGenerateHeaderRow(generateHeaderRow);
    }

    /**
     * @deprecated since 1.2. Use {@link #setGenerateHeaderRow(boolean) instead}
     */
    @Deprecated
    @Override
    public void setGenerateHeaderRow(boolean generateHeaderRow) {
        options.setGenerateHeaderRow(generateHeaderRow);
    }

    public void setOptions(XceliteOptions options) {
        this.options = new XceliteOptions(options);
    }
}
