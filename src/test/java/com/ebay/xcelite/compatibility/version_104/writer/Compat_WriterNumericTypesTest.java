package com.ebay.xcelite.compatibility.version_104.writer;


import com.ebay.xcelite.compatibility.version_104.model.Compat_WriterNumericTypesBean;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Test if all of Java's simple numeric types and all subclasses of
 * {@link Number} get written as numeric Excel cells.
 * Complete based on the Java 1.6 numeric types.
 *
 * Some tests reflect the fact that version 1.0.x did not know about all
 * numeric types in Java and wrote some as String cells.
 * Care must be taken to adapt those tests in future versions that purposely
 * deviate from the 1.0.x behavior
 */

public class Compat_WriterNumericTypesTest extends Compat_AbstractTestBaseForWriterTests {
    static final Compat_WriterNumericTypesBean bean
            = new Compat_WriterNumericTypesBean();

    @BeforeAll
    static public void setup()throws Exception {
        setup(bean);
    }

    /*
    COMPATIBILITY: version 1.0.x passes, version 1.2 and later must conform
    */
    @Test
    public void mustWriteShortsOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        short val = bean.getShortSimpleType();
        Object obj = columnsMap.get("shortSimpleType");
        assertTrue( (obj instanceof Number), "Values of type short must be written as numeric");
        assertEquals(val, ((Number)obj).shortValue());
    }

    /*
    COMPATIBILITY: version 1.0.x passes, version 1.2 and later must conform
    */
    @Test
    public void mustWriteIntOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        int val = bean.getIntegerSimpleType();
        Object obj = columnsMap.get("integerSimpleType");
        assertTrue((obj instanceof Number), "Values of type int must be written as numeric");
        assertEquals(val, ((Number)obj).intValue());
    }

    /*
    COMPATIBILITY: version 1.0.x passes, version 1.2 and later must conform
    */

    @Test
    public void mustWriteLongOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        long val = bean.getLongSimpleType();
        Object obj = columnsMap.get("longSimpleType");
        assertTrue((obj instanceof Number), "Values of type long must be written as numeric");
        assertEquals(val, ((Number)obj).longValue());
    }

    /*
    COMPATIBILITY: version 1.0.x passes, version 1.2 and later must conform
    */
    @Test
    public void mustWriteIntegerOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        int val = bean.getIntegerObjectType();
        Object obj = columnsMap.get("integerObjectType");
        assertTrue((obj instanceof Number), "Values of type Integer must be written as numeric");
        assertEquals(val, ((Number)obj).intValue());
    }

    /*
    COMPATIBILITY: version 1.0.x passes, version 1.2 and later must conform
    */
    @Test
    public void mustWriteLongObjectOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        Long val = bean.getLongObjectType();
        Object obj = columnsMap.get("longObjectType");
        assertTrue((obj instanceof Number), "Values of type Long must be written as numeric");
        assertEquals(val.longValue(), ((Number)obj).longValue());
    }

    /*
   COMPATIBILITY: Version 1.0.x writes BigInteger as String
                  Changed in version 1.2 and later; 1.2 and later must write BigInteger
                  as Double
   */
    @Test
    public void mustWriteBigIntegerTypeOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        BigInteger val = bean.getBigIntegerType();
        Object obj = columnsMap.get("bigIntegerType");
        assertTrue((obj instanceof String), "Values of type BigInteger must be written as numeric");
        assertEquals(val.longValue(), Long.parseLong((String)obj));
    }

    /*
    COMPATIBILITY: Version 1.0.x writes AtomicInteger as String
                   Changed in version 1.2 and later; 1.2 and later must write AtomicInteger
                   as Double
    */
    @Test
    public void mustWriteAtomicIntegerOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        AtomicInteger val = bean.getIntegerAtomicType();
        Object obj = columnsMap.get("integerAtomicType");
        assertTrue((obj instanceof String), "Values of type AtomicInteger must be written as numeric");
        assertEquals(val.intValue(), Integer.parseInt((String)obj));
    }
    /*
    COMPATIBILITY: Version 1.0.x writes AtomicLong as String
                   Changed in version 1.2 and later; 1.2 and later must write AtomicLong
                   as Double
    */
    @Test
    public void mustWriteAtomicLongOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        AtomicLong val = bean.getLongAtomicType();
        Object obj = columnsMap.get("longAtomicType");
        assertTrue((obj instanceof String), "Values of type AtomicLong must be written as numeric");
        assertEquals(val.longValue(), Long.parseLong((String)obj));
    }

    /*
    COMPATIBILITY: version 1.0.x passes, version 1.2 and later must conform
    */
    @Test
    public void mustWriteFloatOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        float val = bean.getFloatSimpleType();
        Object obj = columnsMap.get("floatSimpleType");
        assertTrue((obj instanceof Number), "Values of type float must be written as numeric");
        assertEquals(val, ((Number)obj).floatValue());
    }

    /*
    COMPATIBILITY: version 1.0.x passes, version 1.2 and later must conform
    */
    @Test
    public void mustWriteDoubleOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        double val = bean.getDoubleSimpleType();
        Object obj = columnsMap.get("doubleSimpleType");
        assertTrue((obj instanceof Number), "Values of type double must be written as numeric");
        assertEquals(val, ((Number)obj).doubleValue());
    }

    /*
    COMPATIBILITY: version 1.0.x passes, version 1.2 and later must conform
    */
    @Test
    public void mustWriteFloatObjectOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        Float val = bean.getFloatObjectType();
        Object obj = columnsMap.get("floatObjectType");
        assertTrue((obj instanceof Number), "Values of type Float must be written as numeric");
        assertEquals(val.floatValue(), ((Number)obj).floatValue());
    }

    @Test
    public void mustWriteDoubleObjectOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        Double val = bean.getDoubleObjectType();
        Object obj = columnsMap.get("doubleObjectType");
        assertTrue((obj instanceof Number), "Values of type Double must be written as numeric");
        assertEquals(val.doubleValue(), ((Number)obj).doubleValue());
    }

    /*
    COMPATIBILITY: Version 1.0.x writes BigDecimal as String
                   Changed in version 1.2 and later; 1.2 and later must write BigDecimal
                   as Double
    */
    @Test
    public void mustWriteBigDecimalOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        BigDecimal val = bean.getBigDecimalType();
        Object obj = columnsMap.get("bigDecimalType");
        assertTrue((obj instanceof String), "Values of type BigDecimal must be written as numeric");
        assertEquals(val.doubleValue(), Double.parseDouble((String)obj));
    }

    /*
    COMPATIBILITY: Version 1.0.x writes Number as String
                   Changed in version 1.2 and later; 1.2 and later must write Number
                   as Double
    */
    @Test
    public void mustWritedNumberTypeOK() throws Exception {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        Number val = bean.getNumberType();
        Object obj = columnsMap.get("numberType");
        assertTrue((obj instanceof String), "Values of type Number must be written as numeric");
        assertEquals(val.doubleValue(), Double.parseDouble((String)obj));
    }
}
