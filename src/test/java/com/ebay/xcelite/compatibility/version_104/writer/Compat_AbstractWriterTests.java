package com.ebay.xcelite.compatibility.version_104.writer;

import com.ebay.xcelite.compatibility.version_104.model.Compat_AbstractWriterTestsBean;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Compat_AbstractWriterTests extends Compat_AbstractTestBaseForWriterTests {
    private static Compat_AbstractWriterTestsBean compatBean
            = new Compat_AbstractWriterTestsBean();

    private static com.ebay.xcelite.model.AbstractWriterTestsBean bean
            = new com.ebay.xcelite.model.AbstractWriterTestsBean();

    @BeforeAll
    public static void setup() throws Exception {
        Compat_AbstractTestBaseForWriterTests.setup(compatBean);
        com.ebay.xcelite.writer.AbstractTestBaseForWriterTests.setup(bean);
    }

    /*
    COMPATIBILITY: version 1.0.x writes boolean types as String,
                   version 1.2 and later must write boolean as Boolean cells
    */
    @Test
    public void mustWriteBooleanSimpleTypeOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        boolean val = compatBean.isBooleanSimpleType();
        Object obj = columnsMap.get("booleanSimpleType");
        assertEquals(String.class, obj.getClass(), "Values of type boolean must be written as Boolean");
        assertEquals(Boolean.toString(val), obj);
    }

    /*
    COMPATIBILITY: version 1.0.x passes, version 1.2 and later must conform
    */
    @Test
    public void mustWriteBooleanObjectTypeOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        Boolean val = compatBean.getBooleanObjectType();
        Object obj = columnsMap.get("booleanObjectType");
        assertEquals(val.getClass(), obj.getClass(), "Values of type Boolean must be written as Boolean");
        assertEquals(val, obj);
    }

    /*
    COMPATIBILITY: version 1.0.x passes, version 1.2 and later must conform
    */
    @Test
    public void mustWriteStringOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        String val = compatBean.getStringType();
        Object obj = columnsMap.get("stringType");
        assertEquals(val.getClass(), obj.getClass(), "Values of type String must be written as String");
        assertEquals(val, obj);
    }

}
