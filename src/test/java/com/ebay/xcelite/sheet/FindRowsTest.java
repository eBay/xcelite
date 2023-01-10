/*
 * Copyright 2018 Thanthathon.b.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ebay.xcelite.sheet;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.exceptions.PolicyViolationException;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.model.Person;
import com.ebay.xcelite.model.UsStringCellDateConverter;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.MissingCellPolicy;
import com.ebay.xcelite.policies.MissingRowPolicy;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.writer.TestBaseForWriterTests;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test various combinations of completely empty or not empty rows before and after
 * the column definition row
 *
 * @author Johannes
 */
public class FindRowsTest extends TestBaseForWriterTests {
    private SimpleDateFormat df = new SimpleDateFormat(UsStringCellDateConverter.DATE_PATTERN);
    static File inFile = null;
    static Xcelite xcelite;
    static XceliteSheetImpl sheet;
    static ArrayList<Collection<Collection<Object>>> testData;

    private static String usTestData[][] = {
            {"Crystal",	"Maiden",	"01/02/1990",	"female"},
            {"Witch",	"Doctor",	"01/01/1990",	"male"}
    };

    @BeforeAll
    static void setUp() throws Exception {
        inFile = getResourceFile("RowsBeforeColumnDefinition3.xlsx");

        xcelite = new Xcelite(inFile);
        sheet = (XceliteSheetImpl)xcelite.getSheet(0);
        Row r = sheet.getOrCreateRow(2, false);
        Assertions.assertNull(r);

        SheetReader<Collection<Object>> beanReader = sheet.getSimpleReader();
        testData = (ArrayList<Collection<Collection<Object>>>) new ArrayList(beanReader.read());
    }

    @Test
    @DisplayName("RowIterator of sheet must have as many rows as sheet has")
    public void testRowIterator1() throws Exception {
        int rowCount = sheet.getNativeSheet().getLastRowNum()+1;
        Iterator<Row> iter = sheet.getRowIterator();
        int cnt = 0;
        while (iter.hasNext()) {
            iter.next();
            cnt++;
        }
        Assertions.assertEquals(rowCount, cnt);
    }

    @Test
    @DisplayName("Must correctly retrieve rows from an Excel sheet with null-rows before")
    public void readHeaderWithRowsBeforeAfterMustOK() throws Exception {
        Row r = sheet.getOrCreateRow(2, false);
        Assertions.assertNull(r);

        ArrayList<Collection<Object>> tData = new ArrayList<>(testData.get(3));
        ArrayList foundData = new ArrayList<>();
        Row row = sheet.getOrCreateRow(3, false);
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            foundData.add(cell.getStringCellValue());
        }
        Assertions.assertEquals(tData, foundData);
    }

}
