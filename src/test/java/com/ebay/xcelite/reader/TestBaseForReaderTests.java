package com.ebay.xcelite.reader;

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
    private SimpleDateFormat usDateFormat = new SimpleDateFormat(UsStringCellDateConverter.DATE_PATTERN);

    @SneakyThrows
    public static void setup(XceliteOptions options, Object... inBeans) {
        Xcelite xcelite = new Xcelite();

        if (writeToFile)
            writeWorkbookToFile(workbook);
    }


    @SneakyThrows
    public List<CamelCase> getCamelCaseData(XceliteOptions options, String filePath)  {
        Xcelite xcelite = new Xcelite(new File(filePath));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<CamelCase> beanReader = new BeanSheetReader<>(sheet, options, CamelCase.class);
        ArrayList<CamelCase> data = new ArrayList<>(beanReader.read());
        return data;
    }

    @SneakyThrows
    public void validateCamelCaseData(List<CamelCase> data, String testData[][]) {
        CamelCase first = data.get(0);
        assertEquals(testData[0][0], first.getName(), "Name mismatch");
        assertEquals(testData[0][1], first.getSurname(), "Surname mismatch");
        assertEquals(usDateFormat.parse(testData[0][2]), first.getBirthDate(), "Birthdate mismatch");

        CamelCase second = data.get(1);
        assertEquals(testData[1][0], second.getName(), "Name mismatch");
        assertEquals(testData[1][1], second.getSurname(), "Surname mismatch");
        assertEquals(usDateFormat.parse(testData[1][2]), second.getBirthDate(), "Birthdate mismatch");
    }
}
