package com.ebay.xcelite.reader;

import com.ebay.xcelite.TestSettings;
import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.helper.TestBaseForReaderAndWriterTests;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.model.UsStringCellDateConverter;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBaseForReaderTests extends TestBaseForReaderAndWriterTests {
    @SneakyThrows
    public static void setup(XceliteOptions options, Object... inBeans) {
        Xcelite xcelite = new Xcelite();

        if (TestSettings.WRITE_TO_TEST_FILES)
            writeWorkbookToFile(workbook);
    }
}
