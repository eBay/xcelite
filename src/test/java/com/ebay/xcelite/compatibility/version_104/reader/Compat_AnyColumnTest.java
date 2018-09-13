/*
 * Copyright 2018 Thanthathon.b.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ebay.xcelite.compatibility.version_104.reader;

import com.ebay.xcelite.compatibility.version_104.model.Compat_AnyColumnBean;
import com.ebay.xcelite.compatibility.version_104.model.Compat_AnyColumnBeanDoneWrong;
import com.ebay.xcelite.model.AnyColumnBean;
import com.ebay.xcelite.model.AnyColumnBeanDoneWrong;
import com.ebay.xcelite.reader.AnyColumnTest;
import compat.com.ebay.xcelite_104.Compat_Xcelite;
import compat.com.ebay.xcelite_104.exceptions.Compat_XceliteException;
import compat.com.ebay.xcelite_104.reader.Compat_SheetReader;
import compat.com.ebay.xcelite_104.sheet.Compat_XceliteSheet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 *
 * @author Thanthathon.b
 */
public class Compat_AnyColumnTest {

    private String columnNames[] = new String[]{
            "NAME", "SURNAME", "BIRTHDATE", "SEXID", "SEX"
    };

    private static Object testData[][] = {
            {"Crystal",	"Maiden",	"01/02/1990",	2.0,	"Female"},
            {"Witch",	"Doctor",	"01/01/1990",	1.0,	"Male"}
    };

    /*
        COMPATIBILITY: ColumnsMapper creates a HashMap of header columns, therefore
                        the order is not guaranteed.
                        Changed in version 1.2 and later
     */
    @Test
    public void mustReadColumnHeadersOK() {
        List<String> testColNames = new ArrayList<String>(Arrays.asList(columnNames));
        Compat_Xcelite xcelite = new Compat_Xcelite(new File("src/test/resources/UPPERCASE.xlsx"));
        Compat_XceliteSheet sheet = xcelite.getSheet(0);
        Compat_SheetReader<Compat_AnyColumnBean> beanReader = sheet.getBeanReader(Compat_AnyColumnBean.class);
        Collection<Compat_AnyColumnBean> datasets = beanReader.read();
        for (Compat_AnyColumnBean row : datasets) {
            List<String> dataColNames = new ArrayList<String>(row.getColumns().keySet());
            Assertions.assertEquals(testColNames.size(), dataColNames.size(), "mismatching number of columns");
            for (String col : testColNames) {
                Assertions.assertTrue(dataColNames.contains(col), "unknown column");
            }
            for (String col : dataColNames) {
                Assertions.assertTrue(testColNames.contains(col), "unknown column");
            }
        }
        // call corresponding test for latest version
        AnyColumnTest newVersionTest = new AnyColumnTest();
        newVersionTest.mustReadColumnHeadersOK();
    }


    /*
        COMPATIBILITY: ColumnsMapper creates a HashMap of header columns, therefore
                        the order of data in an AnyColumn is not guaranteed.
                        Changed in version 1.2 and later
     */
    @Test
    public void mustReadDataOK() {
        Compat_Xcelite xcelite = new Compat_Xcelite(new File("src/test/resources/UPPERCASE.xlsx"));
        Compat_XceliteSheet sheet = xcelite.getSheet(0);
        Compat_SheetReader<Compat_AnyColumnBean> beanReader = sheet.getBeanReader(Compat_AnyColumnBean.class);
        Collection<Compat_AnyColumnBean> datasets = beanReader.read();
        int cnt = 0;
        for (Compat_AnyColumnBean row : datasets) {
            List<Object> testColData = new ArrayList<Object>(Arrays.asList(testData[cnt++]));
            List<Object> dataColData = new ArrayList(row.getColumns().values());
            Assertions.assertEquals(testColData.size(), dataColData.size(), "mismatching number of columns");
            for (Object col : testColData) {
                Assertions.assertTrue(dataColData.contains(col), "unknown column");
            }
            for (Object col : dataColData) {
                Assertions.assertTrue(testColData.contains(col), "unknown column");
            }
        }
        // call corresponding test for latest version
        com.ebay.xcelite.reader.AnyColumnTest newVersionTest = new com.ebay.xcelite.reader.AnyColumnTest();
        newVersionTest.mustReadDataOK();
    }

    /*
        COMPATIBILITY: throws an XceliteException.
                        Must be preserved in later versions
    */
    @SuppressWarnings("unchecked")
    @Test
    public void mustThrowOnInvalidBean() {
        Executable testClosure = () -> {
            Compat_Xcelite xcelite = new Compat_Xcelite(new File("src/test/resources/UPPERCASE.xlsx"));
            Compat_XceliteSheet sheet = xcelite.getSheet(0);
            Compat_SheetReader<Compat_AnyColumnBeanDoneWrong> beanReader = sheet.getBeanReader(Compat_AnyColumnBeanDoneWrong.class);
            Collection<Compat_AnyColumnBeanDoneWrong> datasets = beanReader.read();
            int cnt = 0;
            for (Compat_AnyColumnBeanDoneWrong row : datasets) {
                List<Object> testColNames = new ArrayList<Object>(Arrays.asList(testData[cnt++]));
                List<Object> dataColNames = new ArrayList(row.getColumns().values());
                System.out.println(testColNames);
                System.out.println(dataColNames);
            }
        };

        assertThrows(Compat_XceliteException.class, testClosure, "Should have thrown an exception" +
                " because of multiple @AnyColumn annotations");

        // call corresponding test for latest version
        com.ebay.xcelite.reader.AnyColumnTest newVersionTest = new com.ebay.xcelite.reader.AnyColumnTest();
        newVersionTest.mustThrowOnInvalidBean();
    }

}
