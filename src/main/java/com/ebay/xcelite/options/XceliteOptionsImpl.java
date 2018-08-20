package com.ebay.xcelite.options;

public class XceliteOptionsImpl implements XceliteOptions {

    private Integer skipRowsBeforeColDefinitionRow = 0;
    private Integer skipRowsAfterColDefinitionRow = 0;
    private boolean skipBlankRows = true;


    @Override
    public Integer getSkipRowsBeforeColumnDefinitionRow() {
        return skipRowsBeforeColDefinitionRow;
    }

    @Override
    public void setSkipRowsBeforeColumnDefinitionRow(Integer skipRowsBeforeColumnDefinitionRow) {
        this.skipRowsBeforeColDefinitionRow = skipRowsBeforeColumnDefinitionRow;
    }

    @Override
    public Integer getSkipRowsAfterColumnDefinitionRow() {
        return skipRowsAfterColDefinitionRow;
    }

    @Override
    public void setSkipRowsAfterColumnDefinitionRow(Integer skipRowsAfterColumnDefinitionRow) {
        this.skipRowsAfterColDefinitionRow = skipRowsAfterColumnDefinitionRow;
    }

    @Override
    public boolean isSkipBlankRows() {
        return skipBlankRows;
    }

    @Override
    public void setSkipBlankRows(boolean skipBlankRows) {
        this.skipBlankRows = skipBlankRows;
    }

}
