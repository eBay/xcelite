package com.ebay.xcelite.roundtripTests;

import com.ebay.xcelite.helper.TestBaseForReaderAndWriterTests;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.TrailingEmptyRowPolicy;
import documentation_examples.model.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.ebay.xcelite.writer.TestBaseForWriterTests.setupBeans;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeanSheetWriterReaderTests extends TestBaseForReaderAndWriterTests {


    @Test
    @DisplayName("BeanSheetReader must correctly read back data written with BeanSheetWriter, default Options")
    public void basicWriterTest() {
        XceliteOptions options = new XceliteOptions();

        User users[] = createUserClassTestDataWithNullPadding(0, 0);
        setupBeans(options, (Object[])users);

        List<User> readData = getData(User.class, options );
        Assertions.assertEquals(users.length, readData.size(), "number of read rows is wrong");
        validateUserBeanData(users[0], readData.get(0));
        validateUserBeanData(users[1], readData.get(1));
    }

    @Test
    @DisplayName("BeanSheetReader must correctly read back data written with BeanSheetWriter, 1 empty Row before Data")
    public void basicWriterTestWithNullObject() {
        XceliteOptions options = new XceliteOptions();

        User users[] = createUserClassTestDataWithNullPadding(1, 0);
        setupBeans(options, (Object[])users);

        List<User> readData = getData(User.class, options );
        Assertions.assertEquals(users.length, readData.size(), "number of read rows is wrong");
        validateUserBeanData(users[1], readData.get(1));
        validateUserBeanData(users[2], readData.get(2));
    }

    @Test
    @DisplayName("BeanSheetReader must correctly read back data written with BeanSheetWriter, 1 empty data set before, 1 after")
    public void basicWriterTestWithNullObject2() {
        XceliteOptions options = new XceliteOptions();
        options.setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy.NULL);

        User users[] = createUserClassTestDataWithNullPadding(1, 1);
        setupBeans(options, (Object[])users);

        List<User> readData = getData(User.class, options );
        Assertions.assertEquals(users.length, readData.size(), "number of read rows is wrong");
        Assertions.assertNull(readData.get(0));
        validateUserBeanData(users[1], readData.get(1));
        validateUserBeanData(users[2], readData.get(2));
        Assertions.assertNull(readData.get(3));
    }
}
