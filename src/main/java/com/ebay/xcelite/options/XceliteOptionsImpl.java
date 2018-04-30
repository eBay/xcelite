package com.ebay.xcelite.options;

public class XceliteOptionsImpl implements XceliteOptions {
    /**
     * if set, contains the number of lines to skip before
     * the header line is read.
     */
    private Integer skipLinesBeforeHeader = 0;

    /**
     * if set, contains the number of lines to skip after
     * the header line is read and before the first data
     * line is read.
     */
    private Integer skipLinesAfterHeader = 0;

    public XceliteOptionsImpl() {
    }

    @Override
    public Integer getSkipLinesBeforeHeader() {
        return this.skipLinesBeforeHeader;
    }

    @Override
    public Integer getSkipLinesAfterHeader() {
        return this.skipLinesAfterHeader;
    }

    @Override
    public void setSkipLinesBeforeHeader(Integer skipLinesBeforeHeader) {
        this.skipLinesBeforeHeader = skipLinesBeforeHeader;
    }

    @Override
    public void setSkipLinesAfterHeader(Integer skipLinesAfterHeader) {
        this.skipLinesAfterHeader = skipLinesAfterHeader;
    }

}
