package com.ebay.xcelite.helper;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.model.UsStringCellDateConverter;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.reader.AbstractSheetReader;
import com.ebay.xcelite.reader.BeanSheetReader;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import documentation_examples.model.User;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestBaseForReaderAndWriterTests {
    public SimpleDateFormat usDateFormat = new SimpleDateFormat(UsStringCellDateConverter.DATE_PATTERN);

    // set to true to look at the resulting spreadsheet files
    public static boolean writeToFile = true;
    public static XSSFWorkbook workbook;

    @SneakyThrows
    public static void setupSimple(XceliteOptions options, List<Collection<Object>>objs) {
        Xcelite xcelite = new Xcelite();
        XceliteSheet sheet = xcelite.createSheet("Tests");
        SheetWriter bs = sheet.getSimpleWriter();
        bs.setOptions(options);
        bs.write(objs);
        workbook = new XSSFWorkbook(new ByteArrayInputStream(xcelite.getBytes()));
        if (writeToFile)
            writeWorkbookToFile(workbook);
    }

    @SneakyThrows
    public static void setupSimple(XceliteOptions options, Collection<Object>... rowObjects) {
        ArrayList objs = new ArrayList();
        for (Collection<Object> row : rowObjects) {
            objs.add(row);
        }
        Xcelite xcelite = new Xcelite();
        XceliteSheet sheet = xcelite.createSheet("Tests");
        SheetWriter bs = sheet.getSimpleWriter();
        bs.setOptions(options);
        bs.write(objs);
        workbook = new XSSFWorkbook(new ByteArrayInputStream(xcelite.getBytes()));
        if (writeToFile)
            writeWorkbookToFile(workbook);
    }


    public List<Map<String, Object>> extractCellValues (XSSFWorkbook workbook) {
        return extractCellValues (workbook, 0,0);
    }

    public List<Map<String, Object>> extractCellValues (
            XSSFWorkbook workbook,
            int skipBeforeHeader,
            int skipAfterHeader,
            boolean hasHeaderRow) {
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
        if (hasHeaderRow) {
            Row header = iter.next();
            rowId++;
            header.cellIterator().forEachRemaining(cell -> {
                Object val = AbstractSheetReader.readValueFromCell(cell);
                columnNames.add(val.toString());
            });
        }
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

    public List<Map<String, Object>> extractCellValues (
            XSSFWorkbook workbook,
            int skipBeforeHeader,
            int skipAfterHeader) {
        return extractCellValues (workbook,skipBeforeHeader,skipAfterHeader, true);
    }


    public List<Collection<Object>> extractSimpleCellValues (
            XSSFWorkbook workbook,
            int skipBeforeData) {
        List<Collection<Object>> rowVals = new ArrayList<>();
        Sheet excelSheet = workbook.getSheet("Tests");
        Iterator<Row> iter = excelSheet.rowIterator();
        int rowId = 0;
        // move to first row
        while (--skipBeforeData >= 0) {
            rowId++;
            iter.next();
        }
        for (int i = rowId; i <= excelSheet.getLastRowNum(); i++) {
            Row row = excelSheet.getRow(i);
            List<Object> columnsMap = new ArrayList<>();
            rowVals.add(columnsMap);
            if (null != row) {
                Iterator<Cell> cellIter = row.cellIterator();
                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    columnsMap.add(AbstractSheetReader.readValueFromCell(cellIter.next()));
                }
            }
        };

        return rowVals;
    }

    public User[] createUserClassTestDataWithNullPadding(int nullBefore, int nullAfter) {
        User users[] = new User[nullBefore+nullAfter+2];

        for (int i = 0; i < nullBefore; i++)
            users[i] = null;

        User usr3 = new User();
        usr3.setId(1L);
        usr3.setFirstName("Max");
        usr3.setLastName("Busch");
        users[nullBefore] = usr3;

        User usr4 = new User();
        usr4.setId(2L);
        usr4.setFirstName("Moritz");
        usr4.setLastName("Busch");
        users[nullBefore + 1] = usr4;
        for (int i = 0; i < nullAfter; i++)
            users[nullBefore + 2 + i] = null;
        return users;
    }

    public List<Collection<Object>> createSimpleUserTestDataWithNullPadding(int nullBefore, int nullAfter) {
        List<Collection<Object>> users = new ArrayList<>();

        for (int i = 0; i < nullBefore; i++)
            users.add(null);

        List<Object> usr3 = new ArrayList<>();
        usr3.add(1L);
        usr3.add("Max");
        usr3.add("Busch");
        users.add(usr3);

        List<Object> usr4 = new ArrayList<>();
        usr4.add(2L);
        usr4.add("Moritz");
        usr4.add("Busch");
        users.add(usr4);

        for (int i = 0; i < nullAfter; i++)
            users.add(null);
        return users;
    }

    @SneakyThrows
    public List getData(Class clazz, XceliteOptions options, InputStream in) {
        Xcelite xcelite = new Xcelite(in, options);
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader beanReader = sheet.getBeanReader(clazz);
        ArrayList data = new ArrayList<>(beanReader.read());
        return data;
    }

    @SneakyThrows
    public List getData(Class clazz, XceliteOptions options, String filePath) {
        return getData(clazz, options, new FileInputStream(new File(filePath)));
    }

    @SneakyThrows
    public List<CamelCase> getCamelCaseData(XceliteOptions options, String filePath) {
        List<CamelCase> data = getData(CamelCase.class, options, filePath);
        return data;
    }

    @SneakyThrows
    public List getData(Class clazz, XceliteOptions options) {
        Xcelite xcelite =  readXceliteFromWorkbook(options);
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader beanReader = sheet.getBeanReader(clazz);
        ArrayList readData = new ArrayList<>(beanReader.read());
        return readData;
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
        Xcelite xcelite =  readXceliteFromWorkbook(options);
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
    public void validateSimpleUserData2(List<Collection<Object>>data, List<Map<String, Object>> readValues) {
        List<Object> first = (List<Object>)data.get(0);
        Map<String, Object> testFirst = readValues.get(0);
        Assertions.assertEquals(first.get(0), testFirst.get("id"), "Id mismatch");
        Assertions.assertEquals(first.get(1), testFirst.get("Firstname"),  "Name mismatch");
        Assertions.assertEquals(first.get(2), testFirst.get("Lastname"),  "Surname mismatch");
        if (null == first.get(3))
            assertNull(testFirst.get("BirthDate"));
        else
            Assertions.assertEquals(first.get(3), DateUtil.getJavaDate((double)testFirst.get("BirthDate")), "Birthdate mismatch");
        Assertions.assertEquals(((double)first.get(3)), testFirst.get("id"));

        List<Object> second = (List<Object>)data.get(0);
        Map<String, Object> testSecond = readValues.get(0);
        Assertions.assertEquals(second.get(0), testSecond.get("id"), "Id mismatch");
        Assertions.assertEquals(second.get(1), testSecond.get("Firstname"),  "Name mismatch");
        Assertions.assertEquals(second.get(2), testSecond.get("Lastname"),  "Surname mismatch");
        if (null == second.get(3))
            assertNull(testSecond.get("BirthDate"));
        else
            Assertions.assertEquals(second.get(3), DateUtil.getJavaDate((double)testSecond.get("BirthDate")), "Birthdate mismatch");
        Assertions.assertEquals(((double)second.get(3)), testSecond.get("id"));
    }

    @SneakyThrows
    public void validateSimpleUserData(List<Collection<Object>> data, List<Collection<Object>> testData) {
        List<Object> first = (List<Object>)data.get(0);
        List<Object> testFirst = (List<Object>)testData.get(0);
        assertEquals(((Number)first.get(0)).doubleValue(), ((Number)testFirst.get(0)).doubleValue(), "Id mismatch");
        assertEquals(testFirst.get(1), first.get(1),  "Name mismatch");
        assertEquals(testFirst.get(2), first.get(2),  "Surname mismatch");

        List<Object> second = (List<Object>)data.get(1);
        List<Object> testSecond = (List<Object>)testData.get(1);
        assertEquals(((Number)second.get(0)).doubleValue(), ((Number)testSecond.get(0)).doubleValue(), "Id mismatch");
        assertEquals(testSecond.get(1), second.get(1), "Name mismatch");
        assertEquals(testSecond.get(2), second.get(2),  "Surname mismatch");
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
    public void validateSimpleUserData(List<Collection<Object>> data, List<Object> testData[]) {
        List<Object> first = (List<Object>)data.get(0);
        assertEquals(first.get(0), ((Number)testData[0].get(0)).doubleValue(), "Id mismatch");
        assertEquals(first.get(1),testData[0].get(1),  "Name mismatch");
        assertEquals(first.get(2),testData[0].get(2),  "Surname mismatch");

        List<Object> second = (List<Object>)data.get(1);
        assertEquals(second.get(0), ((Number)testData[1].get(0)).doubleValue(),  "Id mismatch");
        assertEquals(second.get(1), testData[1].get(1),  "Name mismatch");
        assertEquals(second.get(2), testData[1].get(2), "Surname mismatch");
    }

    public void validateUserBeanData(User input, Map<String, Object> readValues) {
        Assertions.assertEquals(input.getFirstName(), readValues.get("Firstname"));
        Assertions.assertEquals(input.getLastName(), readValues.get("Lastname"));
        if (null == input.getBirthDate())
            assertNull(readValues.get("BirthDate"));
        else
            Assertions.assertEquals(input.getBirthDate(), DateUtil.getJavaDate((double)readValues.get("BirthDate")));
        Assertions.assertEquals(((double)input.getId()), readValues.get("id"));
    }

    public void validateUserBeanData(User input, User read) {
        Assertions.assertEquals(input.getFirstName(), read.getFirstName());
        Assertions.assertEquals(input.getLastName(), read.getLastName());
        if (null == input.getBirthDate())
            assertNull(read.getBirthDate());
        else
            Assertions.assertEquals(input.getBirthDate(), read.getBirthDate());
        Assertions.assertEquals(input.getId(), read.getId());
    }

    @SneakyThrows
    public static void writeWorkbookToFile(XSSFWorkbook workbook) {
        long tm = System.currentTimeMillis();
        File f = new File("testresult_"+tm + ".xlsx");
        FileOutputStream st = new FileOutputStream(f);
        workbook.write(st);
        st.close();
    }

    @SneakyThrows
    protected Xcelite readXceliteFromWorkbook(XceliteOptions options) {
        byte data[] = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            data = byteArrayOutputStream.toByteArray();
        }
        return new Xcelite(new ByteArrayInputStream(data), options);
    }
}
