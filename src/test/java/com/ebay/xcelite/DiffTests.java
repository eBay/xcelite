package com.ebay.xcelite;

import com.ebay.xcelite.helper.TestUtil;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.utils.diff.DiffResult;
import com.ebay.xcelite.utils.diff.XceliteDiff;
import com.ebay.xcelite.utils.diff.report.ReportGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.util.Collection;

public class DiffTests {

    @Test
    @DisplayName("create a diff between files where the relevant parts are identical")
    void identicalFilesDiffTest() throws Exception {
        XceliteOptions options = new XceliteOptions();
        // just skip rows that are different
        options.setFirstDataRowIndex(3);
        FileInputStream in1 = new FileInputStream(TestUtil.getTestDataFile("RowsBeforeColumnDefinition.xlsx"));
        Xcelite xcelite1 = new Xcelite(in1, options);
        XceliteSheet sheet1 = xcelite1.getSheet(0);
        SheetReader<Collection<Object>> rdr1 = sheet1.getSimpleReader();

        FileInputStream in2 = new FileInputStream(TestUtil.getTestDataFile("RowsBeforeColumnDefinition2.xlsx"));
        Xcelite xcelite2 = new Xcelite(in2, options);
        XceliteSheet sheet2 = xcelite2.getSheet(0);
        SheetReader<Collection<Object>> rdr2 = sheet2.getSimpleReader();

        DiffResult<Collection<Object>> diff = XceliteDiff.diff(rdr1, rdr2, "RowsBeforeColumnDefinition", "RowsBeforeColumnDefinition2");
        Assertions.assertTrue(diff.isIdentical());
    }

    @Test
    @DisplayName("create a diff between files where the relevant parts are different")
    void differingFilesDiffTest() throws Exception {
        XceliteOptions options1 = new XceliteOptions();
        FileInputStream in1 = new FileInputStream(TestUtil.getTestDataFile("UPPERCASE.xlsx"));
        Xcelite xcelite1 = new Xcelite(in1, options1);
        XceliteSheet sheet1 = xcelite1.getSheet(0);
        SheetReader<Collection<Object>> rdr1 = sheet1.getSimpleReader();

        XceliteOptions options2 = new XceliteOptions();
        options2.setFirstDataRowIndex(3);
        FileInputStream in2 = new FileInputStream(TestUtil.getTestDataFile("RowsBeforeColumnDefinition.xlsx"));
        Xcelite xcelite2 = new Xcelite(in2, options2);
        XceliteSheet sheet2 = xcelite2.getSheet(0);
        SheetReader<Collection<Object>> rdr2 = sheet2.getSimpleReader();

        DiffResult<Collection<Object>> diff = XceliteDiff.diff(rdr1, rdr2, "UPPERCASE", "RowsBeforeColumnDefinition");
        Assertions.assertFalse(diff.isIdentical());

        Collection<Collection<Object>> difference = diff.getDifference();
        Assertions.assertEquals(6, difference.size());
    }



}
