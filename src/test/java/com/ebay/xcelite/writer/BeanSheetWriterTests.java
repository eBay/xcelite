package com.ebay.xcelite.writer;

import com.ebay.xcelite.model.BeanWriterTestsBean;
import com.ebay.xcelite.options.XceliteOptions;
import documentation_examples.model.User;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.DateUtil;
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
        User users[] = new User[2];

        User usr3 = new User();
        usr3.setId(1L);
        usr3.setFirstName("Max");
        usr3.setLastName("Busch");
        users[0] = usr3;

        User usr4 = new User();
        usr4.setId(2L);
        usr4.setFirstName("Moritz");
        usr4.setLastName("Busch");
        users[1] = usr4;

        setupBeans(new XceliteOptions(), users);

        List<Map<String, Object>> data = extractCellValues (workbook, 0, 0);
        Assertions.assertEquals(2, data.size(), "number of read rows is wrong");
        validateUserBeanData(users[0], data.get(0));
        validateUserBeanData(users[1], data.get(1));
    }

    @Test
    @DisplayName("Must correctly write with default XceliteOptions")
    public void basicWriterTestWithNullObject() {
        User users[] = new User[3];

        users[0] = null;
        User usr3 = new User();
        usr3.setId(1L);
        usr3.setFirstName("Max");
        usr3.setLastName("Busch");
        users[1] = usr3;

        User usr4 = new User();
        usr4.setId(2L);
        usr4.setFirstName("Moritz");
        usr4.setLastName("Busch");
        users[2] = usr4;

        setupBeans(new XceliteOptions(), users);

        List<Map<String, Object>> data = extractCellValues (workbook, 0, 0);
        Assertions.assertEquals(users.length, data.size(), "number of read rows is wrong");
        assertEquals(0, data.get(0).size());
        validateUserBeanData(users[1], data.get(1));
        validateUserBeanData(users[2], data.get(2));
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


}
