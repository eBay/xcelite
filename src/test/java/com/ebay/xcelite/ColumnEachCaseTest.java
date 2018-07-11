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

import com.ebay.xcelite.exceptions.ColumnNotFoundException;
import com.ebay.xcelite.model.CamelCase;
import com.ebay.xcelite.model.ThaiCase;
import com.ebay.xcelite.model.UpperCase;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import java.io.File;
import java.util.Collection;
import org.junit.Test;

/**
 *
 * @author Thanthathon.b
 */
public class ColumnEachCaseTest {

    @Test
    public void model_UPPER_readUpperMustOK() {
        Collection<UpperCase> upper;
        Xcelite xcelite = new Xcelite(new File("src/test/resources/UPPERCASE.xlsx"));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<UpperCase> beanReader = sheet.getBeanReader(UpperCase.class);
        upper = beanReader.read();
    }
    
    @Test
    public void model_camel_readCamelCaseMustOK() {
        Collection<CamelCase> upper;
        Xcelite xcelite = new Xcelite(new File("src/test/resources/Camel Case.xlsx"));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<CamelCase> beanReader = sheet.getBeanReader(CamelCase.class);
        upper = beanReader.read();
    }
    
    @Test
    public void model_Thai_readThaiCaseMustOK() {
        Collection<ThaiCase> upper;
        Xcelite xcelite = new Xcelite(new File("src/test/resources/Thai Case.xlsx"));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<ThaiCase> beanReader = sheet.getBeanReader(ThaiCase.class);
        upper = beanReader.read();
    }
    
    @Test(expected = ColumnNotFoundException.class)
    public void model_UPPER_readLowerMustFail() {
        Collection<CamelCase> upper;
        Xcelite xcelite = new Xcelite(new File("src/test/resources/UPPERCASE.xlsx"));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<CamelCase> beanReader = sheet.getBeanReader(CamelCase.class);
        upper = beanReader.read();
    }

}
