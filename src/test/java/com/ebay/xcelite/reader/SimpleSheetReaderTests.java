package com.ebay.xcelite.reader;

import com.ebay.xcelite.exceptions.EmptyCellException;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.MissingCellPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
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

        List<Collection<Object>> cc = getSimpleCamelCaseData(options, "src/test/resources/RowsBeforeColumnDefinition3.xlsx");
        validateSimpleCamelCaseData(cc, usTestData);
    }


    @Test
    @DisplayName("Must correctly recognize column headers with not empty rows before and after")
    public void readDataWithRowsBeforeAfterMustOK() throws ParseException {
        XceliteOptions options = new XceliteOptions();
        options.setFirstDataRowIndex(5);

        List<Collection<Object>> cc = getSimpleCamelCaseData(options, "src/test/resources/RowsBeforeColumnDefinition4.xlsx");
        validateSimpleCamelCaseData(cc, usTestData);
    }

}
