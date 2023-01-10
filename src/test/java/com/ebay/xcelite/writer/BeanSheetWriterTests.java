package com.ebay.xcelite.writer;

import com.ebay.xcelite.exceptions.PolicyViolationException;
import com.ebay.xcelite.model.BeanWriterTestsBean;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.MissingCellPolicy;
import com.ebay.xcelite.policies.MissingRowPolicy;
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

import static org.junit.jupiter.api.Assertions.*;

public class BeanSheetWriterTests extends TestBaseForWriterTests {
    private static BeanWriterTestsBean bean = new BeanWriterTestsBean();

    @Test
    @DisplayName("Must correctly write with default XceliteOptions")
    public void basicWriterTest() {
        User[] users = createUserClassTestDataWithNullPadding(0, 0);

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

    /**
     * In the input-data, two User-objects are valid and two are null, so [null, User, User, null]
     * In the output-data, we expect [User, User, User, User], but the first and last (so #0 and #3)
     * must be empty
     */
    @Test
    @DisplayName("Must correctly write with MissingRowPolicy.EMPTY_OBJECT, 1 empty data set before, 1 after")
    public void basicWriterTestWithNullObject3() {
        XceliteOptions options = new XceliteOptions();
        options.setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy.NULL);
        options.setMissingRowPolicy(MissingRowPolicy.EMPTY_OBJECT);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_NULL_AND_BLANK);

        User users[] = createUserClassTestDataWithNullPadding(1, 1);
        setupBeans(options, (Object[])users);

        User emptyUser = new User();
        emptyUser.setFirstName("");
        emptyUser.setLastName("");
        emptyUser.setId(0);
        emptyUser.setBirthDate(null);
        List<Map<String, Object>> readData = extractCellValues (workbook, 0, 0);
        Assertions.assertEquals(users.length, readData.size(), "number of read rows is wrong");
        assertEquals(4, readData.get(0).size());
        assertEquals(4, readData.get(3).size());

        // User objects with data
        validateUserBeanData(users[1], readData.get(1));
        validateUserBeanData(users[2], readData.get(2));

        // empty User objects
        validateUserBeanData(emptyUser, readData.get(0));
        validateUserBeanData(emptyUser, readData.get(3));
    }

    @Test
    @DisplayName("Must correctly throw with only null objects and MissingRowPolicy.EMPTY_OBJECT " +
            "and MissingCellPolicy.RETURN_NULL_AND_BLANK")
    public void basicWriterTestWithNullObject4() {
        XceliteOptions options = new XceliteOptions();
        options.setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy.NULL);
        options.setMissingRowPolicy(MissingRowPolicy.EMPTY_OBJECT);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_NULL_AND_BLANK);

        User users[] = new User[]{null, null, null};

        assertThrows(PolicyViolationException.class, () -> {
            setupBeans(options, User.class, (Object[]) users);
        });
    }

    @Test
    @DisplayName("Must correctly return empty objects with only null objects and " +
            "MissingRowPolicy.EMPTY_OBJECT and MissingCellPolicy.RETURN_BLANK_AS_NULL")
    void basicWriterTestWithNullObject5() {
        XceliteOptions options = new XceliteOptions();
        options.setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy.NULL);
        options.setMissingRowPolicy(MissingRowPolicy.EMPTY_OBJECT);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);

        User[] users = new User[]{null, null, null};
        setupBeans(options, User.class, (Object[]) users);

        List<Map<String, Object>> readData = extractCellValues(workbook, 0, 0);
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
        setupBeans(options, User.class, (Object[]) users);

        List<Map<String, Object>> readData = extractCellValues(workbook, 0, 0);
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
        setupBeans(options, User.class, (Object[]) users);

        List<Map<String, Object>> readData = extractCellValues(workbook, 0, 0);
        Assertions.assertEquals(0, readData.size(), "number of read rows is wrong");
    }


    /**
     * In the input-data, two User-objects are valid and three are null, so
     * [null, User, null, User, null]
     * In the output-data, we expect [User, User]
     */
    @Test
    @DisplayName("Must correctly write with MissingRowPolicy.SKIP, 1 empty data set before, 1 after")
    public void basicWriterTestWithNullObject8() {
        XceliteOptions options = new XceliteOptions();
        options.setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy.NULL);
        options.setMissingRowPolicy(MissingRowPolicy.SKIP);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_NULL_AND_BLANK);

        User users[] = createUserClassTestDataWithNullPadding(0, 0);
        List<User> inData = new ArrayList<>();
        inData.add(null);
        inData.add(users[0]);
        inData.add(null);
        inData.add(users[1]);
        inData.add(null);

        setupBeans(options, inData.toArray());

        List<Map<String, Object>> readData = extractCellValues (workbook, 0, 0);
        Assertions.assertEquals(users.length, readData.size(), "number of read rows is wrong");
        assertEquals(4, readData.get(0).size());
        assertEquals(4, readData.get(1).size());

        validateUserBeanData(inData.get(1), readData.get(0));
        validateUserBeanData(inData.get(3), readData.get(1));
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
