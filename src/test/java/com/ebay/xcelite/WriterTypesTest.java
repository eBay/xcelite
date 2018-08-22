package com.ebay.xcelite;

import com.ebay.xcelite.model.WriterTypesBean;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.options.XceliteOptionsImpl;
import com.ebay.xcelite.reader.AbstractSheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.BeanSheetWriter;
import com.ebay.xcelite.writer.SheetWriter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.*;

public class WriterTypesTest {
    private static final boolean writeToFile = false;
    private static final WriterTypesBean bean = new WriterTypesBean();
    private static XSSFWorkbook workbook;

    @BeforeAll
    @SneakyThrows
    static void setup() {
        Xcelite xcelite = new Xcelite();
        ArrayList<WriterTypesBean> beans = new ArrayList<>();
        XceliteSheet sheet = xcelite.createSheet("Tests");
        beans.add(bean);
        SheetWriter<WriterTypesBean> bs = sheet.getBeanWriter(WriterTypesBean.class);
        bs.write(beans);
        workbook = new XSSFWorkbook(new ByteArrayInputStream(xcelite.getBytes()));
        if (writeToFile)
            writeWorkbookToFile(workbook);
    }

    @Test
    @DisplayName("Must correctly write Java short types")
    void mustWriteShortsOK() {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        short val = bean.getShortSimpleType();
        Object obj = columnsMap.get("shortSimpleType");
        Assertions.assertTrue((obj instanceof Number), "Values of type short must be written as numeric");
        Assertions.assertEquals(val, ((Number)obj).shortValue());
    }

    @Test
    @DisplayName("Must correctly write Java Integer types")
    void mustWriteIntegerOK() {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        int val = bean.getIntegerObjectType();
        Object obj = columnsMap.get("integerObjectType");
        Assertions.assertTrue((obj instanceof Number), "Values of type Integer must be written as numeric");
        Assertions.assertEquals(val, ((Number)obj).intValue());
    }

    @Test
    @DisplayName("Must correctly write Java Long types")
    void mustWriteLongObjectOK() {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        long val = bean.getLongObjectType();
        Object obj = columnsMap.get("longObjectType");
        Assertions.assertTrue((obj instanceof Number), "Values of type Long must be written as numeric");
        Assertions.assertEquals(val, ((Number)obj).longValue());
    }

    @Test
    @DisplayName("Must correctly write Java Short types")
    void mustWriteShortObjectsOK() {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        short val = bean.getShortObjectType();
        Object obj = columnsMap.get("shortObjectType");
        Assertions.assertTrue((obj instanceof Number), "Values of type Short must be written as numeric");
        Assertions.assertEquals(val, ((Number)obj).shortValue());
    }

    @Test
    @DisplayName("Must correctly write Java int types")
    void mustWriteIntOK() {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        int val = bean.getIntegerSimpleType();
        Object obj = columnsMap.get("integerSimpleType");
        Assertions.assertTrue((obj instanceof Number), "Values of type int must be written as numeric");
        Assertions.assertEquals(val, ((Number)obj).intValue());
    }

    @Test
    @DisplayName("Must correctly write Java long types")
    void mustWriteLongOK() {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        long val = bean.getLongSimpleType();
        Object obj = columnsMap.get("longSimpleType");
        Assertions.assertTrue((obj instanceof Number), "Values of type long must be written as numeric");
        Assertions.assertEquals(val, ((Number)obj).longValue());
    }

    private Map<String, Object> extractCellValues (XSSFWorkbook workbook) {
        List<String> columnNames = new ArrayList<>();
        Map<String, Object> columnsMap = new LinkedHashMap<>();
        Sheet excelSheet = workbook.getSheet("Tests");
        Iterator<Row> iter = excelSheet.rowIterator();
        // move to header row
        Row header = iter.next();
        header.cellIterator().forEachRemaining(cell -> {
            Object val = AbstractSheetReader.readValueFromCell(cell);
            columnNames.add(val.toString());
        });
        iter.forEachRemaining(row -> {
            Iterator<Cell> cellIter = row.cellIterator();
            for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                columnsMap.put(columnNames.get(i), AbstractSheetReader.readValueFromCell(cellIter.next()));
            }
        });
        return columnsMap;
    }

    @SneakyThrows
    private static void writeWorkbookToFile(XSSFWorkbook workbook) {
        File f = new File("gotcha.xlsx");
        FileOutputStream st = new FileOutputStream(f);
        workbook.write(st);
        st.close();
    }

}
