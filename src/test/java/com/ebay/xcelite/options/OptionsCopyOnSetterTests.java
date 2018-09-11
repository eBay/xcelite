package com.ebay.xcelite.options;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.policies.MissingCellPolicy;
import com.ebay.xcelite.policies.MissingRowPolicy;
import com.ebay.xcelite.reader.BeanSheetReader;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OptionsCopyOnSetterTests {

    /*
        Defaults:
        private boolean generateHeaderRow = true;
        private boolean headerParsingIsCaseSensitive = true;
        private Integer skipRowsBeforeColDefinitionRow = 0;
        private Integer skipRowsAfterColDefinitionRow = 0;
        private MissingCellPolicy missingCellPolicy = MissingCellPolicy.RETURN_BLANK_AS_NULL;
        private MissingRowPolicy missingRowPolicy = MissingRowPolicy.SKIP;
     */

    @Test
    @DisplayName("Test that copy constructor works")
    void OptionsCopyConstructorTest() {
        XceliteOptions options = new XceliteOptions();

        options.setGenerateHeaderRow(false);
        copyAndTestOptionsEquality(options);

        options.setHeaderParsingIsCaseSensitive(false);
        copyAndTestOptionsEquality(options);

        options.setSkipRowsBeforeColumnDefinitionRow(3);
        copyAndTestOptionsEquality(options);

        options.setSkipRowsAfterColumnDefinitionRow(3);
        copyAndTestOptionsEquality(options);

        options.setMissingCellPolicy(MissingCellPolicy.THROW);
        copyAndTestOptionsEquality(options);

        options.setMissingRowPolicy(MissingRowPolicy.THROW);
        copyAndTestOptionsEquality(options);
    }

    @Test
    @DisplayName("Test Xcelite Setter copies options object")
    void CopyOptionsOnXceliteSetterTest() {
        XceliteOptions options = new XceliteOptions();
        options.setSkipRowsBeforeColumnDefinitionRow(3);
        Xcelite xcelite = new Xcelite();
        xcelite.setOptions(options);

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setSkipRowsBeforeColumnDefinitionRow(4);
        Assertions.assertEquals(oldOptions, xcelite.getOptions(), "Copying options in Setter failed");
        Assertions.assertNotEquals(options, xcelite.getOptions(), "Copying options in Setter failed");
    }

    @Test
    @DisplayName("Test XceliteSheet Setter copies options object")
    void CopyOptionsOnXceliteSheetSetterTest() {
        XceliteOptions options = new XceliteOptions();
        options.setSkipRowsAfterColumnDefinitionRow(3);
        Xcelite xcelite = new Xcelite();
        XceliteSheet sheet = xcelite.createSheet();
        sheet.setOptions(options);

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setSkipRowsBeforeColumnDefinitionRow(4);
        Assertions.assertEquals(oldOptions, sheet.getOptions(), "Copying options in Setter failed");
        Assertions.assertNotEquals(options, sheet.getOptions(), "Copying options in Setter failed");
    }

    @Test
    @DisplayName("Test BeanSheetReader Setter copies options object")
    void CopyOptionsOnBeanSheetReaderSetterTest() {
        XceliteOptions options = new XceliteOptions();
        options.setMissingCellPolicy(MissingCellPolicy.THROW);
        Xcelite xcelite = new Xcelite();
        XceliteSheet sheet = xcelite.createSheet();
        SheetReader<CamelCase> rdr = sheet.getBeanReader(CamelCase.class);
        rdr.setOptions(options);

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setSkipRowsBeforeColumnDefinitionRow(4);
        Assertions.assertEquals(oldOptions, rdr.getOptions(), "Copying options in Setter failed");
        Assertions.assertNotEquals(options, rdr.getOptions(), "Copying options in Setter failed");
    }

    @Test
    @DisplayName("Test SimpleSheetReader Setter copies options object")
    void CopyOptionsOnSimpleSheetReaderSetterTest() {
        XceliteOptions options = new XceliteOptions();
        options.setMissingRowPolicy(MissingRowPolicy.THROW);
        Xcelite xcelite = new Xcelite();
        XceliteSheet sheet = xcelite.createSheet();
        SheetReader rdr = sheet.getSimpleReader();
        rdr.setOptions(options);

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setSkipRowsBeforeColumnDefinitionRow(4);
        Assertions.assertEquals(oldOptions, rdr.getOptions(), "Copying options in Setter failed");
        Assertions.assertNotEquals(options, rdr.getOptions(), "Copying options in Setter failed");
    }

    @Test
    @DisplayName("Test BeanSheetWriter Setter copies options object")
    void CopyOptionsOnBeanSheetWriterSetterTest() {
        XceliteOptions options = new XceliteOptions();
        options.setGenerateHeaderRow(false);
        Xcelite xcelite = new Xcelite();
        XceliteSheet sheet = xcelite.createSheet();
        SheetWriter<CamelCase> writer = sheet.getBeanWriter(CamelCase.class);
        writer.setOptions(options);

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setSkipRowsBeforeColumnDefinitionRow(4);
        Assertions.assertEquals(oldOptions, writer.getOptions(), "Copying options in Setter failed");
        Assertions.assertNotEquals(options, writer.getOptions(), "Copying options in Setter failed");
    }

    @Test
    @DisplayName("Test SimpleSheetReader Setter copies options object")
    void CopyOptionsOnSimpleSheetWriterSetterTest() {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderParsingIsCaseSensitive(false);
        Xcelite xcelite = new Xcelite();
        XceliteSheet sheet = xcelite.createSheet();
        SheetWriter writer = sheet.getSimpleWriter();
        writer.setOptions(options);

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setSkipRowsBeforeColumnDefinitionRow(4);
        Assertions.assertEquals(oldOptions, writer.getOptions(), "Copying options in Setter failed");
        Assertions.assertNotEquals(options, writer.getOptions(), "Copying options in Setter failed");
    }

    private void copyAndTestOptionsEquality(XceliteOptions options) {
        XceliteOptions oldOptions = new XceliteOptions(options);
        Assertions.assertEquals(oldOptions, options, "Copy constructor failed");
    }
}
