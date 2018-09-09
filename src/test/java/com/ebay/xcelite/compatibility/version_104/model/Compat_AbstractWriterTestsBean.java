package com.ebay.xcelite.compatibility.version_104.model;

import compat.com.ebay.xcelite_104.annotations.Compat_Column;

public class Compat_AbstractWriterTestsBean {

    @Compat_Column(name = "booleanSimpleType")
    boolean   booleanSimpleType = true;

    @Compat_Column(name = "booleanObjectType")
    Boolean   booleanObjectType = true;

    @Compat_Column(name = "stringType")
    String   stringType         = "abcde";

    public boolean isBooleanSimpleType() {
        return booleanSimpleType;
    }

    public void setBooleanSimpleType(boolean booleanSimpleType) {
        this.booleanSimpleType = booleanSimpleType;
    }

    public Boolean getBooleanObjectType() {
        return booleanObjectType;
    }

    public void setBooleanObjectType(Boolean booleanObjectType) {
        this.booleanObjectType = booleanObjectType;
    }

    public String getStringType() {
        return stringType;
    }

    public void setStringType(String stringType) {
        this.stringType = stringType;
    }
}
