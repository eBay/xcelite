package com.ebay.xcelite.compatibility.version_104.model;

import compat.com.ebay.xcelite_104.annotations.Compat_Column;

public class Compat_BeanWriterTestsBean {

    @Compat_Column(name = "LONG_STRING")
    private String longString;

    public String getLongString() {
        return longString;
    }

    public void setLongString(String longString) {
        this.longString = longString;
    }
}
