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
        assertEquals(((Number)testData[0].get(0)).doubleValue(), first.get(0), "Id mismatch");
        assertEquals(testData[0].get(1), first.get(1), "Name mismatch");
        assertEquals(testData[0].get(2), first.get(2), "Surname mismatch");

        List<Object> second = (List<Object>)data.get(1);
        assertEquals(((Number)testData[1].get(0)).doubleValue(), second.get(0), "Id mismatch");
        assertEquals(testData[1].get(1), second.get(1), "Name mismatch");
        assertEquals(testData[1].get(2), second.get(2), "Surname mismatch");
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
