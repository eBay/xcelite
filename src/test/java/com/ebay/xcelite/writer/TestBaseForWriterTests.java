package com.ebay.xcelite.writer;

import com.ebay.xcelite.TestSettings;
import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.helper.TestBaseForReaderAndWriterTests;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.XceliteSheet;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;

public class TestBaseForWriterTests extends TestBaseForReaderAndWriterTests {

    @SneakyThrows
    public static void setupBeans(XceliteOptions options, Object... inBeans) {
        Class clazz = null;
        for (Object bean : inBeans) {
            if (null != bean)
                clazz = bean.getClass();
        }
        setupBeans(options, clazz, inBeans);
    }

    @SneakyThrows
    public static void setupBeans(XceliteOptions options, Class clazz, Object... inBeans) {
        Xcelite xcelite = new Xcelite();
        XceliteSheet sheet = xcelite.createSheet("Tests");
        SheetWriter bs = sheet.getBeanWriter(clazz);
        bs.setOptions(options);
        doWrite(xcelite, bs, inBeans);
    }

    @SneakyThrows
    public static void setupBeans(Object... inBeans) {
        setupBeans(new XceliteOptions(), inBeans);
    }

    @SneakyThrows
    public static void setupSimple(XceliteOptions options, Object... inBeans) {
        Xcelite xcelite = new Xcelite();
        XceliteSheet sheet = xcelite.createSheet("Tests");
        SheetWriter bs = sheet.getSimpleWriter();
        bs.setOptions(options);
        doWrite(xcelite, bs, inBeans);
    }

    @SneakyThrows
    public static void setupSimple(Object... inBeans) {
        setupSimple(new XceliteOptions(), inBeans);
    }

    @SneakyThrows
    private static void doWrite(Xcelite xcelite, SheetWriter bs, Object... inBeans) {
        ArrayList beans = new ArrayList();
        for (Object bean : inBeans) {
            beans.add(bean);
        }
        bs.write(beans);
        workbook = new XSSFWorkbook(new ByteArrayInputStream(xcelite.getBytes()));
        if (TestSettings.WRITE_TO_TEST_FILES)
            writeWorkbookToFile(workbook);
    }

}
