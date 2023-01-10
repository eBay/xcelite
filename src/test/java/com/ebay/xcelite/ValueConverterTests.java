package com.ebay.xcelite;

import com.ebay.xcelite.converters.CSVColumnValueConverter;
import com.ebay.xcelite.converters.VTColumnValueConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class ValueConverterTests {

    @Test
    @DisplayName("split a comma-separated String")
    void csvStringTest() throws Exception {
        CSVColumnValueConverter conv = new CSVColumnValueConverter();
        Collection<?> cols = conv.deserialize("a,b,c;d\te");
        List<String> result = new ArrayList(cols);
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("a", result.get(0));
        Assertions.assertEquals("b", result.get(1));
        Assertions.assertEquals("c;d\te", result.get(2));
    }


    @Test
    @DisplayName("split a vt-separated String")
    void vtStringTest() throws Exception {
        VTColumnValueConverter conv = new VTColumnValueConverter();
        Collection<?> cols = conv.deserialize("a,b,c;d\013e");
        List<String> result = new ArrayList(cols);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("a,b,c;d", result.get(0));
        Assertions.assertEquals("e", result.get(1));
    }
}
