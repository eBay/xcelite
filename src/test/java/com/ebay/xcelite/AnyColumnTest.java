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

package com.ebay.xcelite;

import com.ebay.xcelite.model.AnyColumnBean;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Thanthathon.b
 */
public class AnyColumnTest {

    private String columnNames[] = new String[]{
            "NAME", "SURNAME", "BIRTHDATE", "SEXID", "SEX"
    };

    private static String testData[][] = {
            {"Crystal",	"Maiden",	"01/02/1990",	"2",	"Female"},
            {"Witch",	"Doctor",	"01/01/1990",	"1",	"Male"}
    };

    @Test
    @DisplayName("Must correctly parse header row without annotated column headers")
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
    @DisplayName("Must correctly parse data without annotated column headers")
    public void mustReadDataOK() {
        Xcelite xcelite = new Xcelite(new File("src/test/resources/UPPERCASE.xlsx"));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<AnyColumnBean> beanReader = sheet.getBeanReader(AnyColumnBean.class);
        Collection<AnyColumnBean> datasets = beanReader.read();
        int cnt = 0;
        for (AnyColumnBean row : datasets) {
            List<String> testColNames = new ArrayList<>(Arrays.asList(testData[cnt++]));
            List<String> dataColNames = new ArrayList(row.getColumns().values());
            Assertions.assertEquals(testColNames.size(), dataColNames.size(), "mismatching number of columns");
            Assertions.assertEquals(testColNames, dataColNames, "mismatching columns");
        }
    }
}
