package com.ebay.xcelite.writer;

import com.ebay.xcelite.exceptions.PolicyViolationException;
import com.ebay.xcelite.model.BeanWriterTestsBean;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.MissingCellPolicy;
import com.ebay.xcelite.policies.MissingRowPolicy;
import com.ebay.xcelite.policies.TrailingEmptyRowPolicy;
import documentation_examples.model.User;
import lombok.SneakyThrows;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SimpleSheetWriterTests extends TestBaseForWriterTests {

    @Test
    @DisplayName("Must correctly write with default XceliteOptions")
    @SneakyThrows
    void basicWriterTest() {
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
    void basicWriterTestWithNullObject() {
        XceliteOptions options = new XceliteOptions();
        List<Collection<Object>> users = createSimpleUserTestDataWithNullPadding(1,0);

        setupSimple(options, users);

        List<Collection<Object>> readData = extractSimpleCellValues(workbook, 0);
        Assertions.assertEquals(users.size(), readData.size(), "number of read rows is wrong");
        assertEquals(0, readData.get(0).size());
        validateSimpleUserData(readData.subList(1, readData.size()), users.subList(1, users.size()));
    }

    @Test
    @DisplayName("SimpleSheetReader must correctly read back data written with SimpleSheetWriter, 1 empty data set before")
    void basicWriterTestWithNullObject3() {
        XceliteOptions options = new XceliteOptions();
        options.setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy.NULL);
        options.setMissingRowPolicy(MissingRowPolicy.EMPTY_OBJECT);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_NULL_AND_BLANK);

        List<Collection<Object>> users = createSimpleUserTestDataWithNullPadding(1,0);
        setupSimple(options, users);

        List<Collection<Object>> readData = extractSimpleCellValues(workbook, 0);
        Assertions.assertEquals(users.size(), readData.size(), "number of read rows is wrong");
        assertEquals(0, readData.get(0).size());
        validateSimpleUserData(readData.subList(1, readData.size()), users.subList(1, users.size()));
    }

    @Test
    @DisplayName("Must correctly throw with only null objects and MissingRowPolicy.EMPTY_OBJECT")
    void basicWriterTestWithNullObject4() {
        XceliteOptions options = new XceliteOptions();
        options.setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy.NULL);
        options.setMissingRowPolicy(MissingRowPolicy.EMPTY_OBJECT);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_NULL_AND_BLANK);

        User[] users = new User[]{null, null, null};

        assertThrows(PolicyViolationException.class, () -> {
            setupSimple(options, users);
        });
    }

    @Test
    @DisplayName("Must correctly throw with only null objects and MissingRowPolicy.EMPTY_OBJECT")
    void basicWriterTestWithNullObject5() {
        XceliteOptions options = new XceliteOptions();
        options.setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy.NULL);
        options.setMissingRowPolicy(MissingRowPolicy.EMPTY_OBJECT);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);

        User[] users = new User[]{null, null, null};

        setupSimple(options, users);
        List<Collection<Object>> readData = extractSimpleCellValues(workbook, 0);
        Assertions.assertEquals(users.length, readData.size(), "number of read rows is wrong");
        readData.forEach((d) -> {
            assertEquals(0, d.size());
        });
    }

    @Test
    @DisplayName("Must correctly return empty objects with only null objects and " +
            "MissingRowPolicy.EMPTY_OBJECT and MissingCellPolicy.RETURN_BLANK_AS_NULL")
    void basicWriterTestWithNullObject6() {
        XceliteOptions options = new XceliteOptions();
        options.setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy.NULL);
        options.setMissingRowPolicy(MissingRowPolicy.EMPTY_OBJECT);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);

        User[] users = new User[]{null, null, null};

        setupSimple(options, users);
        List<Collection<Object>> readData = extractSimpleCellValues(workbook, 0);
        Assertions.assertEquals(users.length, readData.size(), "number of read rows is wrong");
        readData.forEach((d) -> {
            assertEquals(0, d.size());
        });
    }


    @Test
    @DisplayName("Must correctly return zero objects with only null objects and " +
            "MissingRowPolicy.EMPTY_OBJECT and MissingCellPolicy.RETURN_BLANK_AS_NULL")
    void basicWriterTestWithNullObject7() {
        XceliteOptions options = new XceliteOptions();
        options.setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy.SKIP);
        options.setMissingRowPolicy(MissingRowPolicy.EMPTY_OBJECT);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);

        User[] users = new User[]{null, null, null};

        setupSimple(options, users);
        List<Collection<Object>> readData = extractSimpleCellValues(workbook, 0);
        Assertions.assertEquals(0, readData.size(), "number of read rows is wrong");
    }

}
