package com.ebay.xcelite.reader;

import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.TrailingEmptyRowPolicy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleSheetReaderTests extends TestBaseForReaderTests {

    private static String usTestData[][] = {
            {"Crystal",	"Maiden",	"01/02/1990",	"female"},
            {"Witch",	"Doctor",	"01/01/1990",	"male"}
    };

    @Test
    @DisplayName("Must correctly recognize data with empty rows before and after")
    public void readDataWithEmptyRowsBeforeAfterMustOK() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setFirstDataRowIndex(5);

        List<Collection<Object>> cc = getSimpleData(options, "src/test/resources/RowsBeforeColumnDefinition3.xlsx");
        validateSimpleData(cc, usTestData);
    }

    @Test
    @DisplayName("Must correctly recognize data with empty rows before and after - II")
    public void readDataWithEmptyRowsBeforeOK() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setFirstDataRowIndex(1);

        List<Collection<Object>> cc = getSimpleData(options, "src/test/resources/RowsBeforeData.xlsx");
        validateSimpleData(cc, usTestData);
    }

    @Test
    @DisplayName("Must correctly recognize data with empty rows before and after - III")
    public void readDataWithEmptyRowsBeforeOK2() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setFirstDataRowIndex(5);

        List<Collection<Object>> cc = getSimpleData(options, "src/test/resources/RowsBeforeData2.xlsx");
        validateSimpleData(cc, usTestData);
    }

    @Test
    @DisplayName("Must correctly recognize data with not empty rows before and after")
    public void readDataWithRowsBeforeAfterMustOK() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setFirstDataRowIndex(5);

        List<Collection<Object>> cc = getSimpleData(options, "src/test/resources/RowsBeforeColumnDefinition4.xlsx");
        validateSimpleData(cc, usTestData);
    }

    @Test
    @DisplayName("Must correctly recognize data with empty rows before and after")
    public void readDataWithEmptyRowsAfterOK() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setFirstDataRowIndex(5);
        options.setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy.EMPTY_OBJECT);

        List<Collection<Object>> cc = getSimpleData(options, "src/test/resources/RowsBeforeColumnDefinition3.xlsx");
        Assertions.assertEquals(999, cc.size());
        Assertions.assertNotNull(cc.get(0));
        Assertions.assertNotNull(cc.get(1));
        Assertions.assertNotNull(cc.get(10));
        validateSimpleData(cc, usTestData);
    }


    @Test
    @DisplayName("Must correctly read data from Excel97 format")
    public void readDataFromOLEbasedExcel() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setFirstDataRowIndex(5);

        List<Collection<Object>> cc = getSimpleData(options, "src/test/resources/EmptyRowAfterHeader.xls");
        int cnt = 0;
        for (Collection row : cc) {
            List lRow = new ArrayList(row);
            String[] line = usTestData[cnt];
            for (int i = 0; i < line.length; i++) {
                Assertions.assertEquals(line[i], lRow.get(cnt));
            }
            cnt++;
        }
    }
}
