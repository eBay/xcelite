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
package com.ebay.xcelite.writer;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.exceptions.EmptyCellException;
import com.ebay.xcelite.exceptions.PolicyViolationException;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.model.Person;
import com.ebay.xcelite.model.UsStringCellDateConverter;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.MissingCellPolicy;
import com.ebay.xcelite.policies.MissingRowPolicy;
import com.ebay.xcelite.reader.BeanSheetReader;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test various combinations of completely empty or not empty rows before and after
 * the column definition row
 *
 * @author Johannes
 */
public class PaddingAroundColumnDefinitionWriterTest extends AbstractTestBaseForWriterTests{
    private SimpleDateFormat df = new SimpleDateFormat(UsStringCellDateConverter.DATE_PATTERN);

    private static String usTestData[][] = {
            {"Crystal",	"Maiden",	"01/02/1990",	"female"},
            {"Witch",	"Doctor",	"01/01/1990",	"male"}
    };

    @Test
    @DisplayName("Must correctly write column headers with empty rows before")
    public void writeHeaderWithEmptyRowsBeforeMustOK() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);
        //options.setFirstDataRowIndex(4);

        Person beans[] = new Person[2];
        beans[0] = new Person(usTestData[0][0], usTestData[0][1], df.parse(usTestData[0][2]), usTestData[0][3]);
        beans[1] = new Person(usTestData[1][0], usTestData[1][1], df.parse(usTestData[1][2]), usTestData[1][3]);
        setup(options, beans);

        List<Map<String, Object>> data = extractCellValues (workbook, 2, 0);
        Assertions.assertEquals(2, data.size(), "number of read rows is wrong");
        assertPropertiesMatch(beans[0], data.get(0));
        assertPropertiesMatch(beans[1], data.get(1));
    }

    @Test
    @DisplayName("MissingRowPolicy.EMPTY_OBJECT, MissingCellPolicy.RETURN_BLANK_AS_NULL - Must correctly write data with null objects as empty row")
    public void writeDateWithNullObjectMustWriteEmptyRow() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setMissingRowPolicy(MissingRowPolicy.EMPTY_OBJECT);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);

        Person beans[] = new Person[3];
        beans[0] = null;
        beans[1] = new Person(usTestData[0][0], usTestData[0][1], df.parse(usTestData[0][2]), usTestData[0][3]);
        beans[2] = new Person(usTestData[1][0], usTestData[1][1], df.parse(usTestData[1][2]), usTestData[1][3]);
        setup(options, beans);

        List<Map<String, Object>> data = extractCellValues (workbook, 0, 0);
        Assertions.assertEquals(3, data.size(), "number of read rows is wrong");
        assertPropertiesMatch(new Person(), data.get(0));
        assertPropertiesMatch(beans[1], data.get(1));
        assertPropertiesMatch(beans[2], data.get(2));
    }

   /* @Test
    @DisplayName("MissingRowPolicy.EMPTY_OBJECT, MissingCellPolicy.RETURN_NULL_AND_BLANK - Must correctly write data with null objects as empty row")
    public void writeDateWithNullObjectMustWriteRowWitEmptyCells() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setMissingRowPolicy(MissingRowPolicy.EMPTY_OBJECT);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_NULL_AND_BLANK);

        Person beans[] = new Person[3];
        beans[0] = new Person();
        beans[0].setName("");
        beans[0].setSurname("");
        beans[0].setSex("");
        beans[0].setBirthdate(DateUtil.getJavaDate((double)0));
        beans[1] = new Person(usTestData[0][0], usTestData[0][1], df.parse(usTestData[0][2]), usTestData[0][3]);
        beans[2] = new Person(usTestData[1][0], usTestData[1][1], df.parse(usTestData[1][2]), usTestData[1][3]);
        setup(options, beans);

        List<Map<String, Object>> data = extractCellValues (workbook, 0, 0);
        Assertions.assertEquals(3, data.size(), "number of read rows is wrong");
        assertPropertiesMatch(beans[0], data.get(0));
        assertPropertiesMatch(beans[1], data.get(1));
        assertPropertiesMatch(beans[2], data.get(2));
    }*/


    /*@Test
    @DisplayName("MissingRowPolicy.NULL - Must correctly write data with null objects as null row")
    public void writeDateWithNullObjectMustWriteNullRow() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setMissingRowPolicy(MissingRowPolicy.NULL);

        Person beans[] = new Person[3];
        beans[0] = null;
        beans[1] = new Person(usTestData[0][0], usTestData[0][1], df.parse(usTestData[0][2]), usTestData[0][3]);
        beans[2] = new Person(usTestData[1][0], usTestData[1][1], df.parse(usTestData[1][2]), usTestData[1][3]);
        setup(options, beans);

        List<Map<String, Object>> data = extractCellValues (workbook, 0, 0);
        Assertions.assertEquals(3, data.size(), "number of read rows is wrong");
        assertPropertiesMatch(beans[1], data.get(1));
        assertPropertiesMatch(beans[2], data.get(2));
    }*/


    @Test
    @DisplayName("setMissingRowPolicy.THROW - Must correctly throw writing data with null objects")
    public void mustThrowReadingHeaderRowWithMissingCellsOK() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setMissingRowPolicy(MissingRowPolicy.THROW);

        Person beans[] = new Person[3];
        beans[0] = null;
        beans[1] = new Person(usTestData[0][0], usTestData[0][1], df.parse(usTestData[0][2]), usTestData[0][3]);
        beans[2] = new Person(usTestData[1][0], usTestData[1][1], df.parse(usTestData[1][2]), usTestData[1][3]);

        assertThrows(PolicyViolationException.class, () -> {
            setup(options, beans);
            List<Map<String, Object>> data = extractCellValues (workbook, 0, 0);
        });
    }
/*
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
    }*/


    private void assertPropertiesMatch(Person input, Map<String, Object> readValues) {
        Assertions.assertEquals(input.getName(), readValues.get("Name"));
        Assertions.assertEquals(input.getSurname(), readValues.get("Surname"));
        if (null == input.getBirthdate())
            Assertions.assertNull(readValues.get("Birthdate"));
        else
            Assertions.assertEquals(input.getBirthdate(), DateUtil.getJavaDate((double)readValues.get("Birthdate")));
        Assertions.assertEquals(input.getSex(), readValues.get("Sex"));
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
