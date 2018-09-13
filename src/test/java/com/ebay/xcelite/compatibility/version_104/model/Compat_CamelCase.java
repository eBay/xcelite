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


import compat.com.ebay.xcelite_104.annotations.Compat_Column;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Thanthathon.b
 */
public class Compat_CamelCase implements Serializable {

    @Compat_Column(name = "Name")
    private String name;
    @Compat_Column(name = "Surname")
    private String surname;
    @Compat_Column(dataFormat = Compat_UsStringCellDateConverter.DATE_PATTERN, name = "BirthDate", converter = Compat_UsStringCellDateConverter.class)
    private Date birthDate;
    @Compat_Column(name = "Sex")
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
