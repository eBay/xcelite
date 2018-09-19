package com.ebay.xcelite.writer;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.helper.TestBaseForReaderAndWriterTests;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.sheet.XceliteSheet;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class TestBaseForWriterTests extends TestBaseForReaderAndWriterTests {

    @SneakyThrows
    public static void setup(XceliteOptions options, Object... inBeans) {
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
    public static void setup(Object... inBeans) {
        setup(new XceliteOptions(), inBeans);
    }

}
