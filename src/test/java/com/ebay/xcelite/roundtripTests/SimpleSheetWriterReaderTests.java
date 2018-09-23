package com.ebay.xcelite.roundtripTests;

import com.ebay.xcelite.helper.TestBaseForReaderAndWriterTests;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.writer.TestBaseForWriterTests;
import documentation_examples.model.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.ebay.xcelite.writer.TestBaseForWriterTests.setupBeans;

public class SimpleSheetWriterReaderTests extends TestBaseForReaderAndWriterTests {

    @Test
    @DisplayName("SimpleSheetReader must correctly read back data written with SimpleSheetWriter, default Options")
    @SneakyThrows
    public void basicWriterTestSimple() {
        XceliteOptions options = new XceliteOptions();
        List<Collection<Object>> users = createSimpleUserTestDataWithNullPadding(0,0);

        setupSimple(options, users);

        List<Collection<Object>> readData = getSimpleData(options);

        Assertions.assertEquals(users.size(), readData.size(), "number of read rows is wrong");
        validateSimpleUserData(readData, users);
    }

    @Test
    @DisplayName("Must correctly write with default XceliteOptions")
    public void basicWriterTestWithNullObject() {
        XceliteOptions options = new XceliteOptions();
        List<Collection<Object>> users = createSimpleUserTestDataWithNullPadding(1,0);

        setupSimple(options, users);

        List<Collection<Object>> readData = getSimpleData(options);

        Assertions.assertEquals(users.size(), readData.size(), "number of read rows is wrong");
        Assertions.assertNull(readData.get(0));
        validateSimpleUserData(readData.subList(1, readData.size()), users.subList(1, users.size()));
    }
}
