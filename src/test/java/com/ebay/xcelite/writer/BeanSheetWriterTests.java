package com.ebay.xcelite.writer;

import com.ebay.xcelite.model.BeanWriterTestsBean;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.TrailingEmptyRowPolicy;
import documentation_examples.model.User;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BeanSheetWriterTests extends TestBaseForWriterTests {
    private static BeanWriterTestsBean bean = new BeanWriterTestsBean();

    @Test
    @DisplayName("Must correctly write with default XceliteOptions")
    public void basicWriterTest() {
        User users[] = createUserClassTestDataWithNullPadding(0, 0);

        setupBeans(new XceliteOptions(), (Object[]) users);

        List<Map<String, Object>> data = extractCellValues (workbook, 0, 0);
        Assertions.assertEquals(2, data.size(), "number of read rows is wrong");
        validateUserBeanData(users[0], data.get(0));
        validateUserBeanData(users[1], data.get(1));
    }

    @Test
    @DisplayName("Must correctly write with default XceliteOptions, 1 empty data set before")
    public void basicWriterTestWithNullObject() {
        User users[] = createUserClassTestDataWithNullPadding(1, 0);

        setupBeans(new XceliteOptions(), (Object[]) users);

        List<Map<String, Object>> data = extractCellValues (workbook, 0, 0);
        Assertions.assertEquals(users.length, data.size(), "number of read rows is wrong");
        assertEquals(0, data.get(0).size());
        validateUserBeanData(users[1], data.get(1));
        validateUserBeanData(users[2], data.get(2));
    }

    @Test
    @DisplayName("BeanSheetReader must correctly read back data written with BeanSheetWriter, 1 empty data set before, 1 after")
    public void basicWriterTestWithNullObject2() {
        XceliteOptions options = new XceliteOptions();
        options.setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy.NULL);

        User users[] = createUserClassTestDataWithNullPadding(1, 1);
        setupBeans(options, (Object[])users);

        List<Map<String, Object>> readData = extractCellValues (workbook, 0, 0);
        Assertions.assertEquals(users.length, readData.size(), "number of read rows is wrong");
        assertEquals(0, readData.get(0).size());
        validateUserBeanData(users[1], readData.get(1));
        validateUserBeanData(users[2], readData.get(2));
        assertEquals(0, readData.get(3).size());
    }

    @Test
    @DisplayName("Must correctly write 32KB strings (max length of Excel 2007 format)")
    void mustWriteLongStringsOK() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SpreadsheetVersion.EXCEL2007.getMaxTextLength(); i++) {
            sb.append("a");
        }
        bean.setLongString(sb.toString());
        setupBeans(bean);
        List<Map<String, Object>> rowList = extractCellValues (workbook);
        Map<String, Object> columnsMap = rowList.get(0);
        String val = bean.getLongString();
        Object obj = columnsMap.get("LONG_STRING");
        Assertions.assertEquals(val, obj);
    }


    @Test
    @DisplayName("Must correctly write header with bold formatting")
    public void basicWriterTestCheckHeaderFormat() {
        User users[] = createUserClassTestDataWithNullPadding(1, 0);

        setupBeans(new XceliteOptions(), (Object[]) users);

        XSSFSheet sheet = workbook.getSheetAt(0);
        Row r = sheet.getRow(0);
        Iterator<Cell> iter = r.cellIterator();
        while (iter.hasNext()) {
            Cell c = iter.next();
            CellStyle style = c.getCellStyle();
            int fontIndex = style.getFontIndexAsInt();
            XSSFFont font = workbook.getFontAt(fontIndex);
            Assertions.assertTrue(font.getBold());
        }
    }
}
