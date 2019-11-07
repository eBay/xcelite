package com.ebay.xcelite.annotations;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.exceptions.ColumnNotFoundException;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.model.CamelCaseOptionalColumn;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.reader.TestBaseForReaderTests;
import com.ebay.xcelite.sheet.XceliteSheet;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OptionalColumnAnnotationTest extends TestBaseForReaderTests {

    private static String testData1[][] = {
            {"Crystal",	null, "01/02/1990",	"female"},
            {"Witch",	null, "01/01/1990",	"male"}
    };


    private static String testData2[][] = {
            {"Crystal",	"Maiden",	"01/02/1990",	"female"},
            {"Witch",	"Doctor",	"01/01/1990",	"male"}
    };

    @Test
    @DisplayName("Optional Column missing")
    void mustReadMissingOptionalColumn() {
        XceliteOptions options = new XceliteOptions();
        try {
            List<CamelCaseOptionalColumn> upper = getOptionalColumnData(CamelCaseOptionalColumn.class, "src/test/resources/CamelCaseNoSurname.xlsx", options);
            validateCamelCaseOptionalColumnData(upper, testData1);
        } catch (Exception ex) {
            Assertions.fail(ex);
        }
    }

    @Test
    @DisplayName("Optional Column present")
    void mustReadOptionalColumn() {
        XceliteOptions options = new XceliteOptions();
        try {
            List<CamelCaseOptionalColumn> upper = getOptionalColumnData(CamelCaseOptionalColumn.class, "src/test/resources/Camel Case.xlsx", options);
            validateCamelCaseOptionalColumnData(upper, testData2);
        } catch (Exception ex) {
            Assertions.fail(ex);
        }
    }

    @Test
    @DisplayName("Optional Column missing, throws")
    void mustThrowMissingOptionalColumn() {
        XceliteOptions options = new XceliteOptions();
        assertThrows(ColumnNotFoundException.class, () -> {
            List<CamelCase> upper = getOptionalColumnData(CamelCase.class, "src/test/resources/CamelCaseNoSurname.xlsx", options);
            validateCamelCaseData(upper, testData1);
        });
    }


    @SneakyThrows
    public List getOptionalColumnData(Class clazz, String filePath, XceliteOptions options) {
        File f = new File(filePath);
        Xcelite xcelite =  new Xcelite(new FileInputStream(f));
        xcelite.setOptions(options);
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader beanReader = sheet.getBeanReader(clazz);
        ArrayList readData = new ArrayList<>(beanReader.read());
        return readData;
    }


    @SneakyThrows
    public void validateCamelCaseOptionalColumnData(List<CamelCaseOptionalColumn> data, String testData[][]) {
        CamelCaseOptionalColumn first = data.get(0);
        assertEquals(testData[0][0], first.getName(), "Name mismatch");
        assertEquals(testData[0][1], first.getSurname(), "Surname mismatch");
        assertEquals(usDateFormat.parse(testData[0][2]), first.getBirthDate(), "Birthdate mismatch");

        CamelCaseOptionalColumn second = data.get(1);
        assertEquals(testData[1][0], second.getName(), "Name mismatch");
        assertEquals(testData[1][1], second.getSurname(), "Surname mismatch");
        assertEquals(usDateFormat.parse(testData[1][2]), second.getBirthDate(), "Birthdate mismatch");
    }
}
