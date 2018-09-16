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
import com.ebay.xcelite.exceptions.ColumnNotFoundException;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.model.ThaiCase;
import com.ebay.xcelite.model.UpperCase;
import com.ebay.xcelite.model.UsStringCellDateConverter;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.XceliteSheet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test various upper/lower/camel-casings of the header-row
 * that defines the columns and mapping to model properties
 *
 * n.b. the provided Excel-sheets have a lookup of the sex
 * via the sexid and the second sheet. Date column is not
 * date formatted, but text.
 *
 * @author Thanthathon.b
 */
public class StringColumnCapitalizationTest {
    SimpleDateFormat usDateFormat = new SimpleDateFormat(UsStringCellDateConverter.DATE_PATTERN);

    private static String usTestData[][] = {
            {"Crystal",	"Maiden",	"01/02/1990",	"2",	"Female"},
            {"Witch",	"Doctor",	"01/01/1990",	"1",	"Male"}
    };

    private static String thaiTestData[][] = {
            {"แม่มด",	"น้ำแข็ง","02/01/1447",	"2",	"Female"},
            {"พ่อมด",	"หมอ",	"01/01/1447",	"1",	"Male"}
    };

    @Test
    @DisplayName("Must correctly recognize uppercase column headers")
    public void model_UPPER_readUpperMustOK() throws ParseException {
        Xcelite xcelite = new Xcelite(new File("src/test/resources/UPPERCASE.xlsx"));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<UpperCase> beanReader = sheet.getBeanReader(UpperCase.class);
        ArrayList<UpperCase> upper = new ArrayList<>(beanReader.read());

        UpperCase first = upper.get(0);
        assertEquals(usTestData[0][0], first.getName(), "Name mismatch");
        assertEquals(usTestData[0][1], first.getSurname(), "Surname mismatch");
        assertEquals(usDateFormat.parse(usTestData[0][2]), first.getBirthDate(), "Birthdate mismatch");

        UpperCase second = upper.get(1);
        assertEquals(usTestData[1][0], second.getName(), "Name mismatch");
        assertEquals(usTestData[1][1], second.getSurname(), "Surname mismatch");
        assertEquals(usDateFormat.parse(usTestData[1][2]), second.getBirthDate(), "Birthdate mismatch");
    }
    
    @Test
    @DisplayName("Must correctly recognize camelcase column headers")
    public void model_camel_readCamelCaseMustOK() throws ParseException {
        Xcelite xcelite = new Xcelite(new File("src/test/resources/Camel Case.xlsx"));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<CamelCase> beanReader = sheet.getBeanReader(CamelCase.class);
        ArrayList<CamelCase> upper = new ArrayList<>(beanReader.read());

        CamelCase first = upper.get(0);
        assertEquals(usTestData[0][0], first.getName(), "Name mismatch");
        assertEquals(usTestData[0][1], first.getSurname(), "Surname mismatch");
        assertEquals(usDateFormat.parse(usTestData[0][2]), first.getBirthDate(), "Birthdate mismatch");

        CamelCase second = upper.get(1);
        assertEquals(usTestData[1][0], second.getName(), "Name mismatch");
        assertEquals(usTestData[1][1], second.getSurname(), "Surname mismatch");
        assertEquals(usDateFormat.parse(usTestData[1][2]), second.getBirthDate(), "Birthdate mismatch");
    }
    
    @Test
    @DisplayName("Must correctly recognize thai column headers")
    public void model_Thai_readThaiCaseMustOK() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(UsStringCellDateConverter.DATE_PATTERN);
        Xcelite xcelite = new Xcelite(new File("src/test/resources/Thai Case.xlsx"));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<ThaiCase> beanReader = sheet.getBeanReader(ThaiCase.class);
        ArrayList<ThaiCase> thais = new ArrayList<>(beanReader.read());

        ThaiCase first = thais.get(0);
        assertEquals(thaiTestData[0][0], first.getName(), "Name mismatch");
        assertEquals(thaiTestData[0][1], first.getSurname(), "Surname mismatch");
        assertEquals(df.parse(thaiTestData[0][2]), first.getBirthDate(), "Birthdate mismatch");

        ThaiCase second = thais.get(1);
        assertEquals(thaiTestData[1][0], second.getName(), "Name mismatch");
        assertEquals(thaiTestData[1][1], second.getSurname(), "Surname mismatch");
        assertEquals(df.parse(thaiTestData[1][2]), second.getBirthDate(), "Birthdate mismatch");

    }

    @Test
    @DisplayName("When header parsing is case-sensitive, this must throw")
    public void HeaderParsingIsCaseSensitive_UPPER_readLowerMustFail() throws ParseException {
        Xcelite xcelite = new Xcelite(new File("src/test/resources/UPPERCASE.xlsx"));
        XceliteSheet sheet = xcelite.getSheet(0);
        XceliteOptions options = sheet.getOptions();
        options.setHeaderParsingIsCaseSensitive(true);

        assertThrows(ColumnNotFoundException.class, () -> {
            SheetReader<CamelCase> beanReader = sheet.getBeanReader(CamelCase.class);
            beanReader.read();
        });
    }

    @Test
    @DisplayName("When header parsing is not case-sensitive, this must pass")
    public void HeaderParsingIsNOTCaseSensitive_UPPER_readLowerMustNotFail() throws ParseException {
        Xcelite xcelite = new Xcelite(new File("src/test/resources/UPPERCASE.xlsx"));
        XceliteSheet sheet = xcelite.getSheet(0);
        XceliteOptions options = sheet.getOptions();
        options.setHeaderParsingIsCaseSensitive(false);
        SheetReader<CamelCase> beanReader = new BeanSheetReader<>(sheet, options, CamelCase.class);
        Collection<CamelCase> data = beanReader.read();
        assertEquals(2, data.size(), "Wrong row count");
        Iterator<CamelCase> iter = data.iterator();

        CamelCase first = iter.next();
        assertEquals(usTestData[0][0], first.getName(), "Name mismatch");
        assertEquals(usTestData[0][1], first.getSurname(), "Surname mismatch");
        assertEquals(usDateFormat.parse(usTestData[0][2]), first.getBirthDate(), "Birthdate mismatch");

        CamelCase second = iter.next();
        assertEquals(usTestData[1][0], second.getName(), "Name mismatch");
        assertEquals(usTestData[1][1], second.getSurname(), "Surname mismatch");
        assertEquals(usDateFormat.parse(usTestData[1][2]), second.getBirthDate(), "Birthdate mismatch");
    }
}
