package com.ebay.xcelite.writer;

import com.ebay.xcelite.model.BeanWriterTestsBean;
import com.ebay.xcelite.options.XceliteOptions;
import documentation_examples.model.User;
import lombok.SneakyThrows;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SimpleSheetWriterTests extends TestBaseForWriterTests {
    private static BeanWriterTestsBean bean = new BeanWriterTestsBean();

    @Test
    @DisplayName("Must correctly write with default XceliteOptions")
    @SneakyThrows
    public void basicWriterTest() {
        XceliteOptions options = new XceliteOptions();
        List<Object> users[] = new List[2];

        List<Object> usr3 = new ArrayList<>();
        usr3.add(1L);
        usr3.add("Max");
        usr3.add("Busch");
        users[0] = usr3;

        List<Object> usr4 = new ArrayList<>();
        usr4.add(2L);
        usr4.add("Moritz");
        usr4.add("Busch");
        users[1] = usr4;

        setupSimple(options, users);

        List<Collection<Object>> readData = getSimpleData(options);
        Assertions.assertEquals(2, readData.size(), "number of read rows is wrong");
        validateSimpleUserData(readData, users);
    }
/*
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
        assertPropertiesMatch(users[1], data.get(1));
        assertPropertiesMatch(users[2], data.get(2));
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
*/

}
