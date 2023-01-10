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

package com.ebay.xcelite.model;

import com.ebay.xcelite.annotations.AnyColumn;
import com.ebay.xcelite.annotations.Column;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Johannes
 */
@Data
public class AnyColumnEmployeeBean {

    @Column(name = "first_name")
    private String name;

    @Column(name = "last_name")
    private String surname;

    @Column(name = "company_name")
    private String companyName;

    @AnyColumn
    private Map<String,List<String>> projects;
    
}
