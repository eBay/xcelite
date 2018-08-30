/*
  Copyright [2013-2014] eBay Software Foundation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.ebay.xcelite.styles;

import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * Class description...
 *
 * @author kharel (kharel@ebay.com)
 * created Sep 9, 2013
 */
public final class CellStylesBank {

    private static Map<Workbook, CellStyles> cellStylesMap;

    static {
        cellStylesMap = Maps.newHashMap();
    }

    public static CellStyles get(Workbook workbook) {
        if (cellStylesMap.containsKey(workbook)) {
            return cellStylesMap.get(workbook);
        }
        CellStyles cellStyles = new CellStyles(workbook);
        cellStylesMap.put(workbook, cellStyles);
        return cellStyles;
    }
}
