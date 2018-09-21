package com.ebay.xcelite.helper;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.model.UsStringCellDateConverter;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.reader.AbstractSheetReader;
import com.ebay.xcelite.reader.BeanSheetReader;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBaseForReaderAndWriterTests {
    public SimpleDateFormat usDateFormat = new SimpleDateFormat(UsStringCellDateConverter.DATE_PATTERN);

    // set to true to look at the resulting spreadsheet files
    public static boolean writeToFile = false;
    public static XSSFWorkbook workbook;


    @SneakyThrows
    public static void setup(Object... inBeans) {
        setup(new XceliteOptions(), inBeans);
    }

    public List<Map<String, Object>> extractCellValues (XSSFWorkbook workbook) {
        return extractCellValues (workbook, 0,0);
    }

    public List<Map<String, Object>> extractCellValues (
            XSSFWorkbook workbook,
            int skipBeforeHeader,
            int skipAfterHeader) {
        List<Map<String, Object>> rowVals = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        Sheet excelSheet = workbook.getSheet("Tests");
        Iterator<Row> iter = excelSheet.rowIterator();
        int rowId = 0;
        // move to header row
        while (--skipBeforeHeader >= 0) {
            rowId++;
            iter.next();
        }
        Row header = iter.next();
        rowId++;
        header.cellIterator().forEachRemaining(cell -> {
            Object val = AbstractSheetReader.readValueFromCell(cell);
            columnNames.add(val.toString());
        });
        while (--skipAfterHeader >= 0) {
            rowId++;
            iter.next();
        }
        for (int i = rowId; i <= excelSheet.getLastRowNum(); i++) {
            Row row = excelSheet.getRow(i);
            Map<String, Object> columnsMap = new LinkedHashMap<>();
            rowVals.add(columnsMap);
            if (null != row) {
                Iterator<Cell> cellIter = row.cellIterator();
                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    columnsMap.put(columnNames.get(j), AbstractSheetReader.readValueFromCell(cellIter.next()));
                }
            }
        };
        return rowVals;
    }

    @SneakyThrows
    public List getData(Class clazz, XceliteOptions options, String filePath) {
        Xcelite xcelite = new Xcelite(new FileInputStream(new File(filePath)), options);
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader beanReader = sheet.getBeanReader(clazz);
        ArrayList data = new ArrayList<>(beanReader.read());
        return data;
    }

    @SneakyThrows
    public List<CamelCase> getCamelCaseData(XceliteOptions options, String filePath) {
        List<CamelCase> data = getData(CamelCase.class, options, filePath);
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

    @SneakyThrows
    public List<Collection<Object>> getSimpleData(XceliteOptions options) {
        byte data[] = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            data = byteArrayOutputStream.toByteArray();
        }
        Xcelite xcelite = new Xcelite(new ByteArrayInputStream(data), options);
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<Collection<Object>> simpleReader = sheet.getSimpleReader();
        ArrayList<Collection<Object>> readData = new ArrayList<>(simpleReader.read());
        return readData;
    }



    @SneakyThrows
    public List<Collection<Object>> getSimpleData(XceliteOptions options, InputStream in) {
        Xcelite xcelite = new Xcelite(in, options);
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<Collection<Object>> simpleReader = sheet.getSimpleReader();
        ArrayList<Collection<Object>> data = new ArrayList<>(simpleReader.read());
        return data;
    }

    @SneakyThrows
    public List<Collection<Object>> getSimpleData(XceliteOptions options, String filePath) {
        return getSimpleData (options, new FileInputStream(new File(filePath)));
    }

    @SneakyThrows
    public void validateSimpleData(List<Collection<Object>> data, String testData[][]) {
        List<Object> first = (List<Object>)data.get(0);
        assertEquals(testData[0][0], first.get(0), "Name mismatch");
        assertEquals(testData[0][1], first.get(1), "Surname mismatch");
        assertEquals(testData[0][2], first.get(2), "Birthdate mismatch");

        List<Object> second = (List<Object>)data.get(1);
        assertEquals(testData[1][0], second.get(0), "Name mismatch");
        assertEquals(testData[1][1], second.get(1), "Surname mismatch");
        assertEquals(testData[1][2], second.get(2), "Birthdate mismatch");
    }

    @SneakyThrows
    public void validateSimpleUserData(List<Collection<Object>> data, List<Object>  testData[]) {
        List<Object> first = (List<Object>)data.get(0);
        assertEquals(((Number)testData[0].get(0)).doubleValue(), first.get(0), "Id mismatch");
        assertEquals(testData[0].get(1), first.get(1), "Name mismatch");
        assertEquals(testData[0].get(2), first.get(2), "Surname mismatch");

        List<Object> second = (List<Object>)data.get(1);
        assertEquals(((Number)testData[1].get(0)).doubleValue(), second.get(0), "Id mismatch");
        assertEquals(testData[1].get(1), second.get(1), "Name mismatch");
        assertEquals(testData[1].get(2), second.get(2), "Surname mismatch");
    }

    @SneakyThrows
    public static void writeWorkbookToFile(XSSFWorkbook workbook) {
        long tm = System.currentTimeMillis();
        File f = new File("testresult_"+tm + ".xlsx");
        FileOutputStream st = new FileOutputStream(f);
        workbook.write(st);
        st.close();
    }
}
