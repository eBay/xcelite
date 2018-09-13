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
package com.ebay.xcelite.reader;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.model.UsStringCellDateConverter;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.XceliteSheet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test various combinations of completely empty or not empty rows before and after
 * the column definition row
 *
 * @author Johannes
 */
public class PaddingAroundColumnDefinitionTest {
    private SimpleDateFormat df = new SimpleDateFormat(UsStringCellDateConverter.DATE_PATTERN);

    private static String usTestData[][] = {
            {"Crystal",	"Maiden",	"01/02/1990",	"female"},
            {"Witch",	"Doctor",	"01/01/1990",	"male"}
    };

    @Test
    @DisplayName("Must correctly recognize  column headers with empty rows before")
    public void readHeaderWithEmptyRowsBeforeMustOK() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);

        List<CamelCase> upper = getData(options, "src/test/resources/RowsBeforeColumnDefinition.xlsx");
        validateData(upper);
    }


    @Test
    @DisplayName("Must correctly recognize column headers with not empty rows before")
    public void readHeaderWithRowsBeforeMustOK() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);

        List<CamelCase> upper = getData(options, "src/test/resources/RowsBeforeColumnDefinition2.xlsx");
        validateData(upper);
    }

    @Test
    @DisplayName("Must correctly recognize column headers with empty rows before and after")
    public void readHeaderWithEmptyRowsBeforeAfterMustOK() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);
        options.setFirstDataRowIndex(5);

        List<CamelCase> upper = getData(options, "src/test/resources/RowsBeforeColumnDefinition3.xlsx");
        validateData(upper);
    }


    @Test
    @DisplayName("Must correctly recognize column headers with not empty rows before and after")
    public void readHeaderWithRowsBeforeAfterMustOK() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);
        options.setFirstDataRowIndex(5);

        List<CamelCase> upper = getData(options, "src/test/resources/RowsBeforeColumnDefinition4.xlsx");
        validateData(upper);
    }

    private List<CamelCase> getData(XceliteOptions options, String filePath) {
        Xcelite xcelite = new Xcelite(new File(filePath));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<CamelCase> beanReader = new BeanSheetReader<>(sheet, options, CamelCase.class);
        ArrayList<CamelCase> data = new ArrayList<>(beanReader.read());
        return data;
    }

    private void validateData(List<CamelCase> data) throws ParseException {
        CamelCase first = data.get(0);
        assertEquals(usTestData[0][0], first.getName(), "Name mismatch");
        assertEquals(usTestData[0][1], first.getSurname(), "Surname mismatch");
        assertEquals(df.parse(usTestData[0][2]), first.getBirthDate(), "Birthdate mismatch");

        CamelCase second = data.get(1);
        assertEquals(usTestData[1][0], second.getName(), "Name mismatch");
        assertEquals(usTestData[1][1], second.getSurname(), "Surname mismatch");
        assertEquals(df.parse(usTestData[1][2]), second.getBirthDate(), "Birthdate mismatch");
    }
}
