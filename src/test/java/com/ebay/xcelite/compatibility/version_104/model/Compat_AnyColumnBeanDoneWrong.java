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

package com.ebay.xcelite.compatibility.version_104.model;


import compat.com.ebay.xcelite_104.annotations.Compat_AnyColumn;

import java.util.List;
import java.util.Map;

/**
 * Using this bean causes an exception, as multiple
 * AnyColumn annotations are not allowed
 *
 * @author Johannes
 */

public class Compat_AnyColumnBeanDoneWrong {

    @Compat_AnyColumn
    private Map<String,List<String>> columns;

    @Compat_AnyColumn
    private Map<String,List<String>> moreColumns;

    public Map<String, List<String>> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, List<String>> columns) {
        this.columns = columns;
    }
}
