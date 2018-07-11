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

import com.ebay.xcelite.annotations.Column;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Thanthathon.b
 */
public class ThaiCase implements Serializable {

    @Column(name = "ชื่อ")
    private String name;
    @Column(name = "นามสกุล")
    private String surname;
    @Column(dataFormat = DateConverter.DATE_PATTERN, name = "วันเกิด", converter = DateConverter.class)
    private Date birthDate;
    @Column(name = "เพศ")
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
