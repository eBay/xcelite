package com.ebay.xcelite.reader;

import com.ebay.xcelite.exceptions.EmptyCellException;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.MissingCellPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanSheetReaderTests extends TestBaseForReaderTests {

    private static String usTestData[][] = {
            {"Crystal",	"Maiden",	"01/02/1990",	"female"},
            {"Witch",	"Doctor",	"01/01/1990",	"male"}
    };

    @Test
    @DisplayName("MissingCellPolicy default - Must correctly read a header row where some columns are not mapped to annotated properties")
    void mustReadHeaderRowWithMissingCellsOK() {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);

        List<CamelCase> upper = getCamelCaseData(options, "src/test/resources/ColumnDefinitionRowHasEmptyCells.xlsx");
        validateCamelCaseData(upper, usTestData);
    }

    @Test
    @DisplayName("MissingCellPolicy.RETURN_BLANK_AS_NULL - Must correctly read a header row where some columns are not mapped to annotated properties")
    void mustReadHeaderRowWithMissingCellsWithMissingCellPolicy_RETURN_BLANK_AS_NULL_OK() {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);

        List<CamelCase> upper = getCamelCaseData(options, "src/test/resources/ColumnDefinitionRowHasEmptyCells.xlsx");
        validateCamelCaseData(upper, usTestData);
    }

    @Test
    @DisplayName("MissingCellPolicy.RETURN_NULL_AND_BLANK - Must correctly read a header row where some columns are not mapped to annotated properties")
    void mustReadHeaderRowWithMissingCellsWithMissingCellPolicy_RETURN_NULL_AND_BLANK_OK() {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_NULL_AND_BLANK);

        List<CamelCase> upper = getCamelCaseData(options, "src/test/resources/ColumnDefinitionRowHasEmptyCells.xlsx");
        validateCamelCaseData(upper, usTestData);
    }

    @Test
    @DisplayName("MissingCellPolicy.THROW - Must correctly throw reading a header row where some columns are not mapped to annotated properties")
    public void mustThrowReadingHeaderRowWithMissingCellsOK() {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);
        options.setMissingCellPolicy(MissingCellPolicy.THROW);

        assertThrows(EmptyCellException.class, () -> {
            List<CamelCase> upper = getCamelCaseData(options, "src/test/resources/ColumnDefinitionRowHasEmptyCells.xlsx");
            validateCamelCaseData(upper, usTestData);
        });
    }
}
