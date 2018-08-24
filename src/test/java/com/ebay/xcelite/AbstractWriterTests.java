package com.ebay.xcelite;

import com.ebay.xcelite.model.AbstractWriterTestsBean;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

class AbstractWriterTests extends AbstractTestBaseForWriterTests {
    private static AbstractWriterTestsBean bean = new AbstractWriterTestsBean();

    @BeforeAll
    @SneakyThrows
    static void setup() {
        setup(bean, bean.getClass());
    }

    @Test
    @DisplayName("Must correctly write boolean)")
    void mustWriteBooleanSimpleTypeOK() {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        boolean val = bean.isBooleanSimpleType();
        Object obj = columnsMap.get("booleanSimpleType");
        Assertions.assertEquals(Boolean.class, obj.getClass(), "Values of type boolean must be written as Boolean");
        Assertions.assertEquals(val, obj);
    }

    @Test
    @DisplayName("Must correctly write Boolean)")
    void mustWriteBooleanObjectTypeOK() {
        Map<String, Object> columnsMap = extractCellValues (workbook);
        Boolean val = bean.getBooleanObjectType();
        Object obj = columnsMap.get("booleanObjectType");
        Assertions.assertEquals(val.getClass(), obj.getClass(), "Values of type Boolean must be written as Boolean");
        Assertions.assertEquals(val, obj);
    }


}
