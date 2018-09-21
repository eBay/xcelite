package com.ebay.xcelite.roundtripTests;

import com.ebay.xcelite.helper.TestBaseForReaderAndWriterTests;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.writer.TestBaseForWriterTests;
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

public class SimpleWriterReaderTests extends TestBaseForReaderAndWriterTests {

    @Test
    @DisplayName("SimpleSheetReader must correctly read back data written with SimpleSheetWriter, default Options")
    @SneakyThrows
    public void basicWriterTestSimple() {
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

}
