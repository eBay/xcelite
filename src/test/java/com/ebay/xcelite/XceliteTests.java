package com.ebay.xcelite;

import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.MissingCellPolicy;
import com.ebay.xcelite.policies.MissingRowPolicy;
import com.ebay.xcelite.policies.TrailingEmptyRowPolicy;
import com.ebay.xcelite.sheet.XceliteSheet;
import org.apache.poi.ss.SpreadsheetVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class XceliteTests {

    @Test
    @DisplayName("Must correctly create default Options")
    void creationMustCreateDefaultOptions() {
        XceliteOptions options = new XceliteOptions();

        Xcelite x = new Xcelite();
        Assertions.assertEquals(options, x.getOptions());
        Assertions.assertEquals(new XceliteOptions(), x.getOptions());
    }

    @Test
    @DisplayName("Must correctly take the XceliteOptions")
    void creationMustTakeOptions() {
        XceliteOptions options = new XceliteOptions();
        options.setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy.NULL);
        options.setMissingRowPolicy(MissingRowPolicy.SKIP);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_NULL_AND_BLANK);

        Xcelite x = new Xcelite(options);
        Assertions.assertEquals(options, x.getOptions());
        Assertions.assertNotEquals(new XceliteOptions(), x.getOptions());
    }

    @Test
    @DisplayName("Must correctly create sheets")
    void creationMustCreateSheets() {
        String[] sheetNames = new String[]{"Sheet1", "Sheet2", "Sheet3"};

        Xcelite x = new Xcelite();
        for (String name : sheetNames) {
            x.createSheet(name);
        }
        List<XceliteSheet> sheets = x.getSheets();
        Assertions.assertEquals(3, sheets.size());
    }

    @Test
    @DisplayName("Must correctly return sheets")
    void creationMustReturnSheets() {
        String[] sheetNames = new String[]{"Sheet1", "Sheet2", "Sheet3"};

        Xcelite x = new Xcelite();
        for (String name : sheetNames) {
            x.createSheet(name);
        }
        for (String name : sheetNames) {
            XceliteSheet s = x.getSheet(name);
            Assertions.assertEquals(s.getSheetName(), name);
        }
    }

    @Test
    @DisplayName("Must correctly return sheet by index")
    void creationMustReturnSheets2() {
        String[] sheetNames = new String[]{"Sheet1", "Sheet2", "Sheet3"};

        Xcelite x = new Xcelite();
        for (String name : sheetNames) {
            x.createSheet(name);
        }
        for (int idx = 0; idx < sheetNames.length; idx++) {
            XceliteSheet s = x.getSheet(idx);
            Assertions.assertEquals(s.getSheetName(), sheetNames[idx]);
        }
    }
}
