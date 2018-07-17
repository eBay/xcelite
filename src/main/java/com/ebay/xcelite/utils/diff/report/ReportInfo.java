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
package com.ebay.xcelite.utils.diff.report;

import com.ebay.xcelite.utils.diff.info.Collections;
import com.ebay.xcelite.utils.diff.info.Info;
import com.ebay.xcelite.utils.diff.info.Sheets;
import com.ebay.xcelite.utils.diff.info.StringTuple;

/**
 * Class description...
 *
 * @author kharel (kharel@ebay.com)
 * created Nov 21, 2013
 */
public class ReportInfo<T> implements Info<T> {

    private final StringTuple stringTuple;
    private final Sheets sheets;
    private final Collections<T> collections;

    public ReportInfo(StringTuple stringTuple, Sheets sheets, Collections<T> collections) {
        this.stringTuple = stringTuple;
        this.sheets = sheets;
        this.collections = collections;
    }

    @Override
    public StringTuple files() {
        return stringTuple;
    }

    @Override
    public Sheets sheets() {
        return sheets;
    }

    @Override
    public Collections<T> collections() {
        return collections;
    }
}
