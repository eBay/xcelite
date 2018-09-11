package com.ebay.xcelite.options;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.policies.MissingCellPolicy;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class OptionsCascadingTests {

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
    @DisplayName("Test copying options from Xcelite to Sheet works")
    void mustCascadeOptionsFromXceliteToSheet() {
        XceliteOptions options = new XceliteOptions();
        options.setSkipRowsBeforeColumnDefinitionRow(3);
        Xcelite xcelite = new Xcelite();
        xcelite.setOptions(options);
        XceliteSheet sheet = xcelite.createSheet();

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setSkipRowsBeforeColumnDefinitionRow(4);
        Assertions.assertEquals(xcelite.getOptions(), sheet.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertEquals(oldOptions, sheet.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertNotEquals(options, sheet.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertNotEquals(options, xcelite.getOptions(), "Cascading options in Sheet constructor failed");
    }

    @Test
    @DisplayName("Test copying options from Xcelite to BeanReader works")
    void mustCascadeOptionsFromXceliteToBeanReader() {
        XceliteOptions options = new XceliteOptions();
        options.setSkipRowsBeforeColumnDefinitionRow(3);
        Xcelite xcelite = new Xcelite();
        xcelite.setOptions(options);
        XceliteSheet sheet = xcelite.createSheet();
        SheetReader<CamelCase> reader = sheet.getBeanReader(CamelCase.class);

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setSkipRowsBeforeColumnDefinitionRow(4);
        Assertions.assertEquals(xcelite.getOptions(), reader.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertEquals(sheet.getOptions(), reader.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertEquals(oldOptions, reader.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertNotEquals(options, reader.getOptions(), "Cascading options in Sheet constructor failed");
    }

    @Test
    @DisplayName("Test copying options from Xcelite to SimpleReader works")
    void mustCascadeOptionsFromXceliteToSimpleReader() {
        XceliteOptions options = new XceliteOptions();
        options.setSkipRowsBeforeColumnDefinitionRow(3);
        Xcelite xcelite = new Xcelite();
        xcelite.setOptions(options);
        XceliteSheet sheet = xcelite.createSheet();
        SheetReader reader = sheet.getSimpleReader();

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setSkipRowsBeforeColumnDefinitionRow(4);
        Assertions.assertEquals(xcelite.getOptions(), reader.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertEquals(sheet.getOptions(), reader.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertEquals(oldOptions, reader.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertNotEquals(options, reader.getOptions(), "Cascading options in Sheet constructor failed");
    }

    @Test
    @DisplayName("Test copying options from Xcelite to BeanWriter works")
    void mustCascadeOptionsFromXceliteToBeanWriter() {
        XceliteOptions options = new XceliteOptions();
        options.setSkipRowsBeforeColumnDefinitionRow(3);
        Xcelite xcelite = new Xcelite();
        xcelite.setOptions(options);
        XceliteSheet sheet = xcelite.createSheet();
        SheetWriter<CamelCase> writer = sheet.getBeanWriter(CamelCase.class);

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setSkipRowsBeforeColumnDefinitionRow(4);
        Assertions.assertEquals(xcelite.getOptions(), writer.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertEquals(sheet.getOptions(), writer.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertEquals(oldOptions, writer.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertNotEquals(options, writer.getOptions(), "Cascading options in Sheet constructor failed");
    }

    @Test
    @DisplayName("Test copying options from Xcelite to SimpleWriter works")
    void mustCascadeOptionsFromXceliteToSimpleWriter() {
        XceliteOptions options = new XceliteOptions();
        options.setSkipRowsBeforeColumnDefinitionRow(3);
        Xcelite xcelite = new Xcelite();
        xcelite.setOptions(options);
        XceliteSheet sheet = xcelite.createSheet();
        SheetWriter writer = sheet.getSimpleWriter();

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setSkipRowsBeforeColumnDefinitionRow(4);
        Assertions.assertEquals(xcelite.getOptions(), writer.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertEquals(sheet.getOptions(), writer.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertEquals(oldOptions, writer.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertNotEquals(options, writer.getOptions(), "Cascading options in Sheet constructor failed");
    }
}
