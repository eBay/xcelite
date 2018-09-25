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
        options.setHeaderRowIndex(3);
        Xcelite xcelite = new Xcelite();
        xcelite.setOptions(options);
        XceliteSheet sheet = xcelite.createSheet();

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setHeaderRowIndex(4);
        options.setMissingCellPolicy(MissingCellPolicy.THROW);
        Assertions.assertEquals(xcelite.getOptions(), sheet.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertEquals(oldOptions, sheet.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertNotEquals(options, sheet.getOptions(), "Cascading options in Sheet constructor failed");
        Assertions.assertNotEquals(options, xcelite.getOptions(), "Cascading options in Sheet constructor failed");
    }

    @Test
    @DisplayName("Test copying options from Xcelite to BeanReader works")
    void mustCascadeOptionsFromXceliteToBeanReader() {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);
        Xcelite xcelite = new Xcelite();
        xcelite.setOptions(options);
        XceliteSheet sheet = xcelite.createSheet();
        SheetReader<CamelCase> reader = sheet.getBeanReader(CamelCase.class);

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setHeaderRowIndex(4);
        options.setMissingCellPolicy(MissingCellPolicy.THROW);
        XceliteOptions readerOptions = reader.getOptions();
        readerOptions.setFirstDataRowIndex(xcelite.getOptions().getFirstDataRowIndex());
        Assertions.assertEquals(xcelite.getOptions(), readerOptions, "Cascading options from Xcelite in Sheet constructor failed");
        Assertions.assertEquals(sheet.getOptions(), readerOptions, "Cascading options from Sheet in Sheet constructor failed");
        Assertions.assertEquals(oldOptions, readerOptions, "Cascading options in Sheet constructor failed: this should not happen");
        Assertions.assertNotEquals(options, readerOptions, "Cascading options in Sheet constructor failed: Xcelite changed options");
    }

    @Test
    @DisplayName("Test copying options from Xcelite to SimpleReader works")
    void mustCascadeOptionsFromXceliteToSimpleReader() {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);
        Xcelite xcelite = new Xcelite();
        xcelite.setOptions(options);
        XceliteSheet sheet = xcelite.createSheet();
        SheetReader reader = sheet.getSimpleReader();

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setHeaderRowIndex(4);
        options.setMissingCellPolicy(MissingCellPolicy.THROW);
        XceliteOptions readerOptions = reader.getOptions();
        readerOptions.setFirstDataRowIndex(xcelite.getOptions().getFirstDataRowIndex());
        Assertions.assertEquals(xcelite.getOptions(), readerOptions, "Cascading options from Xcelite in Sheet constructor failed");
        Assertions.assertEquals(sheet.getOptions(), readerOptions, "Cascading options from Sheet in Sheet constructor failed");
        Assertions.assertEquals(oldOptions, readerOptions, "Cascading options in Sheet constructor failed: this should not happen");
        Assertions.assertNotEquals(options, readerOptions, "Cascading options in Sheet constructor failed: Xcelite changed options");
    }

    @Test
    @DisplayName("Test copying options from Xcelite to BeanWriter works")
    void mustCascadeOptionsFromXceliteToBeanWriter() {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);
        Xcelite xcelite = new Xcelite();
        xcelite.setOptions(options);
        XceliteSheet sheet = xcelite.createSheet();
        SheetWriter<CamelCase> writer = sheet.getBeanWriter(CamelCase.class);

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setHeaderRowIndex(4);
        options.setMissingCellPolicy(MissingCellPolicy.THROW);
        XceliteOptions writerOptions = writer.getOptions();
        writerOptions.setFirstDataRowIndex(xcelite.getOptions().getFirstDataRowIndex());
        Assertions.assertEquals(xcelite.getOptions(), writerOptions, "Cascading options from Xcelite in Sheet constructor failed");
        Assertions.assertEquals(sheet.getOptions(), writerOptions, "Cascading options from Sheet in Sheet constructor failed");
        Assertions.assertEquals(oldOptions, writerOptions, "Cascading options in Sheet constructor failed: this should not happen");
        Assertions.assertNotEquals(options, writerOptions, "Cascading options in Sheet constructor failed: Xcelite changed options");
    }

    @Test
    @DisplayName("Test copying options from Xcelite to SimpleWriter works")
    void mustCascadeOptionsFromXceliteToSimpleWriter() {
        XceliteOptions options = new XceliteOptions();
        options.setHeaderRowIndex(3);
        Xcelite xcelite = new Xcelite();
        xcelite.setOptions(options);
        XceliteSheet sheet = xcelite.createSheet();
        SheetWriter writer = sheet.getSimpleWriter();

        XceliteOptions oldOptions = new XceliteOptions(options);
        options.setHeaderRowIndex(4);
        options.setMissingCellPolicy(MissingCellPolicy.THROW);
        XceliteOptions writerOptions = writer.getOptions();
        writerOptions.setFirstDataRowIndex(xcelite.getOptions().getFirstDataRowIndex());
        Assertions.assertEquals(xcelite.getOptions(), writerOptions, "Cascading options from Xcelite in Sheet constructor failed");
        Assertions.assertEquals(sheet.getOptions(), writerOptions, "Cascading options from Sheet in Sheet constructor failed");
        Assertions.assertEquals(oldOptions, writerOptions, "Cascading options in Sheet constructor failed: this should not happen");
        Assertions.assertNotEquals(options, writerOptions, "Cascading options in Sheet constructor failed: Xcelite changed options");
    }
}
