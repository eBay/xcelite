package com.ebay.xcelite.options;

import lombok.Data;

public interface XceliteOptions {

    /**
     * if set, contains the number of lines to skip before
     * the header line is read.
     */
    Integer skipLinesBeforeHeader = 0;

    /**
     * if set, contains the number of lines to skip after
     * the header line is read and before the first data
     * line is read.
     */
    Integer skipLinesAfterHeader = 0;

    public Integer getSkipLinesBeforeHeader();

    public Integer getSkipLinesAfterHeader();

    public void setSkipLinesBeforeHeader(Integer skipLinesBeforeHeader);

    public void setSkipLinesAfterHeader(Integer skipLinesAfterHeader);
}
