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
import java.io.File;
import java.util.Collection;
import org.junit.Test;

/**
 *
 * @author Thanthathon.b
 */
public class AnyColumnTest {

    @Test
    public void model_AnyColumn_readMustOK() {
        Collection<AnyColumnBean> bean;
        Xcelite xcelite = new Xcelite(new File("src/test/resources/UPPERCASE.xlsx"));
        XceliteSheet sheet = xcelite.getSheet(0);
        SheetReader<AnyColumnBean> beanReader = sheet.getBeanReader(AnyColumnBean.class);
        bean = beanReader.read();
        for (AnyColumnBean col : bean) {
            System.out.println("COL : " + col.getColumns().keySet());
        }
    }
}
