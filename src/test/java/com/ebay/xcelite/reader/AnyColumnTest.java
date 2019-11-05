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

package com.ebay.xcelite.reader;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.exceptions.XceliteException;
import com.ebay.xcelite.model.AnyColumnBean;
import com.ebay.xcelite.model.AnyColumnBeanDoneWrong;
import com.ebay.xcelite.model.AnyColumnEmployeeBean;
import com.ebay.xcelite.options.XceliteOptions;
import com.ebay.xcelite.policies.MissingCellPolicy;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
 public class AnyColumnTest {

    private String columnNames[] = new String[]{
            "NAME", "SURNAME", "BIRTHDATE", "SEXID", "SEX"
    };

    private static Object testData[][] = {
            {"Crystal",	"Maiden",	"01/02/1990",	2.0,	"Female"},
            {"Witch",	"Doctor",	"01/01/1990",	1.0,	"Male"}
    };

    private static String employeeProjects1[] = {
            null,
            "Testing",
            "Website Relaunch",
            null,
            null
    };
    private static String employeeProjects2[][] = {
            {"Website Relaunch", "Testing",          null},
            {null,               "Migration",       "Testing"},
            {"Testing",           null,             "Website Relaunch"},
            {null,                null,              null},
            {null,               "Website Relaunch", null}
    };

    @Test
    @DisplayName("Must correctly parse header row with @AnyColumn annotated column headers")
    public void mustReadColumnHeadersOK() {
        List<String> testColNames = new ArrayList<>(Arrays.asList(columnNames));
        Xcelite xcelite = new Xcelite(new File("src/test/resources/UPPERCASE.xlsx"));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<AnyColumnBean> beanReader = sheet.getBeanReader(AnyColumnBean.class);
        Collection<AnyColumnBean> datasets = beanReader.read();
        for (AnyColumnBean row : datasets) {
            List<String> dataColNames = new ArrayList<>(row.getColumns().keySet());
            Assertions.assertEquals(testColNames.size(), dataColNames.size(), "mismatching number of columns");
            Assertions.assertEquals(testColNames, dataColNames, "mismatching columns");
        }
    }

    @Test
    @DisplayName("Must correctly parse data with @AnyColumn annotated column headers")
    @SuppressWarnings("unchecked")
    public void mustReadDataOK() {
        Xcelite xcelite = new Xcelite(new File("src/test/resources/UPPERCASE.xlsx"));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<AnyColumnBean> beanReader = sheet.getBeanReader(AnyColumnBean.class);
        Collection<AnyColumnBean> datasets = beanReader.read();
        int cnt = 0;
        for (AnyColumnBean row : datasets) {
            List<Object> testColNames = new ArrayList<>(Arrays.asList(testData[cnt++]));
            List<Object> dataColNames = new ArrayList(row.getColumns().values());
            Assertions.assertEquals(testColNames.size(), dataColNames.size(), "mismatching number of columns");
            Assertions.assertEquals(testColNames, dataColNames, "mismatching columns");
        }
    }

    @Test
    @DisplayName("Must throw an exception because of multiple @AnyColumn annotations")
    @SuppressWarnings("unchecked")
    public void mustThrowOnInvalidBean() {
        Executable testClosure = () -> {
            Xcelite xcelite = new Xcelite(new File("src/test/resources/UPPERCASE.xlsx"));
            XceliteSheet sheet = xcelite.getSheet(0);
            SheetReader<AnyColumnBeanDoneWrong> beanReader = sheet.getBeanReader(AnyColumnBeanDoneWrong.class);
            Collection<AnyColumnBeanDoneWrong> datasets = beanReader.read();
            int cnt = 0;
            for (AnyColumnBeanDoneWrong row : datasets) {
                List<Object> testColNames = new ArrayList<>(Arrays.asList(testData[cnt++]));
                List<Object> dataColNames = new ArrayList(row.getColumns().values());
                System.out.println(testColNames);
                System.out.println(dataColNames);
            }
        };

        assertThrows(XceliteException.class, testClosure, "Should have thrown an exception" +
                " because of multiple @AnyColumn annotations");

    }

    @Test
    @DisplayName("Must correctly overwrite fields with @AnyColumn and repeated column headers")
    @SuppressWarnings("unchecked")
    /**
     * When `anyColumnCreatesCollection` from XceliteOptions is `false`, must correctly
     * overwrite fields with @AnyColumn and repeated column headers
     * @since 1.3
     */
    public void mustReadAnyColumnDataOkNoCollections() {
        XceliteOptions options = new XceliteOptions();
        options.setAnyColumnCreatesCollection(false);
        Xcelite xcelite = new Xcelite(new File("src/test/resources/employees.xlsx"));
        xcelite.setOptions(options);
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<AnyColumnEmployeeBean> beanReader = sheet.getBeanReader(AnyColumnEmployeeBean.class);
        Collection<AnyColumnEmployeeBean> datasets = beanReader.read();
        int cnt = 0;
        for (AnyColumnEmployeeBean row : datasets) {
            List<Object> dataColValues = new ArrayList(row.getProjects().values());
            Assertions.assertEquals(1, dataColValues.size(), "mismatching number of columns");
            Assertions.assertEquals(employeeProjects1[cnt], dataColValues.get(0), "mismatching columns");
            cnt++;
        }
    }

    @Test
    @DisplayName("Must correctly create collections for fields with @AnyColumn and repeated column headers")
    @SuppressWarnings("unchecked")
    /**
     * When `anyColumnCreatesCollection` from XceliteOptions is `true`, must correctly
     * create Collections for fields with @AnyColumn and repeated column headers
     * @since 1.3
     */
    public void mustReadAnyColumnDataOkWithCollections() {
        XceliteOptions options = new XceliteOptions();
        options.setAnyColumnCreatesCollection(true);
        options.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
        Xcelite xcelite = new Xcelite(new File("src/test/resources/employees.xlsx"));
        xcelite.setOptions(options);
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<AnyColumnEmployeeBean> beanReader = sheet.getBeanReader(AnyColumnEmployeeBean.class);
        Collection<AnyColumnEmployeeBean> datasets = beanReader.read();
        int cnt = 0;
        for (AnyColumnEmployeeBean row : datasets) {
            List<Object> dataColValues = new ArrayList(row.getProjects().values().iterator().next());
            List<String> testRow = Arrays.asList(employeeProjects2[cnt]);
            Assertions.assertEquals(testRow, dataColValues, "mismatching columns");
            cnt++;
        }
    }
}
