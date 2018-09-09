package com.ebay.xcelite.compatibility.version_104.writer;

import com.ebay.xcelite.compatibility.version_104.model.Compat_BeanWriterTestsBean;
import org.apache.poi.ss.SpreadsheetVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;


@DisplayName("Compatibility BeanWriterTests test compatibility with Version 1.0.4")
public class Compat_BeanWriterTests extends Compat_AbstractTestBaseForWriterTests {
    private static Compat_BeanWriterTestsBean bean = new Compat_BeanWriterTestsBean();

    @BeforeAll
    public static void setup() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SpreadsheetVersion.EXCEL2007.getMaxTextLength(); i++) {
            sb.append("a");
        }
        bean.setLongString(sb.toString());
        setup(bean);
    }

    @Test
    public void mustWriteLongStringsOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        String val = bean.getLongString();
        Object obj = columnsMap.get("LONG_STRING");
        Assertions.assertEquals(val, obj);
    }


}
