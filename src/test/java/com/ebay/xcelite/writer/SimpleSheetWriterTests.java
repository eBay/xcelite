package com.ebay.xcelite.writer;

import com.ebay.xcelite.model.BeanWriterTestsBean;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.TrailingEmptyRowPolicy;
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

    @Test
    @DisplayName("Must correctly write with default XceliteOptions")
    @SneakyThrows
    public void basicWriterTest() {
        XceliteOptions options = new XceliteOptions();
        List<Collection<Object>> users = createSimpleUserTestDataWithNullPadding(0,0);
        setupSimple(options, users);

        List<Collection<Object>> readData = extractSimpleCellValues(workbook, 0);
        Assertions.assertEquals(users.size(), readData.size(), "number of read rows is wrong");
        validateSimpleUserData(users, readData);
        validateSimpleUserData(users, readData);
    }

    @Test
    @DisplayName("SimpleSheetReader must correctly read back data written with SimpleSheetWriter, 1 empty data set before")
    public void basicWriterTestWithNullObject() {
        XceliteOptions options = new XceliteOptions();
        List<Collection<Object>> users = createSimpleUserTestDataWithNullPadding(1,0);

        setupSimple(options, users);

        List<Collection<Object>> readData = extractSimpleCellValues(workbook, 0);
        Assertions.assertEquals(users.size(), readData.size(), "number of read rows is wrong");
        assertEquals(0, readData.get(0).size());
        validateSimpleUserData(readData.subList(1, readData.size()), users.subList(1, users.size()));
    }
}
