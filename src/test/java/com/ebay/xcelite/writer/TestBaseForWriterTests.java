package com.ebay.xcelite.writer;

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
        Xcelite xcelite = new Xcelite();
        ArrayList beans = new ArrayList();
        XceliteSheet sheet = xcelite.createSheet("Tests");
        Class clazz = null;
        for (Object bean : inBeans) {
            beans.add(bean);
            if (null != bean)
                clazz = bean.getClass();
        }
        SheetWriter bs = sheet.getBeanWriter(clazz);
        bs.setOptions(options);
        bs.write(beans);
        workbook = new XSSFWorkbook(new ByteArrayInputStream(xcelite.getBytes()));
        if (writeToFile)
            writeWorkbookToFile(workbook);
    }

    @SneakyThrows
    public static void setupBeans(Object... inBeans) {
        setupBeans(new XceliteOptions(), inBeans);
    }

    @SneakyThrows
    public static void setupSimple(XceliteOptions options, Collection<Object>... rowObjects) {
        ArrayList objs = new ArrayList();
        for (Collection<Object> row : rowObjects) {
            objs.add(row);
        }
        Xcelite xcelite = new Xcelite();
        XceliteSheet sheet = xcelite.createSheet("Tests");
        SheetWriter bs = sheet.getSimpleWriter();
        bs.setOptions(options);
        bs.write(objs);
        workbook = new XSSFWorkbook(new ByteArrayInputStream(xcelite.getBytes()));
        if (writeToFile)
            writeWorkbookToFile(workbook);
    }

    @SneakyThrows
    public static void setupSimple(Object... inBeans) {
        setupSimple(new XceliteOptions(), inBeans);
    }

}
