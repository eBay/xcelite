package com.ebay.xcelite.options;

/**
 * Configuration interface for setting options of a {@link com.ebay.xcelite.Xcelite}
 * object.
 *
 * @since 1.2
 */

public interface XceliteOptions {

    /**
     * Returns the number of rows Xcelite should skip before trying
     * to parse the next one as the row defining the annotated bean
     * properties
     *
     * @return The number of rows
     */
    public Integer getSkipRowsBeforeColumnDefinitionRow();

    /**
     * Sets the number of rows Xcelite should skip before trying
     * to parse the next one as the row defining the annotated bean
     * properties
     *
     * @param skipRowsBeforeColumnDefinitionRow The number of rows to skip
     */
    public void setSkipRowsBeforeColumnDefinitionRow (Integer skipRowsBeforeColumnDefinitionRow);

    /**
     * Returns the number of rows Xcelite should skip after
     * the row defining the annotated bean properties is parsed and
     * before the first data row is read.
     * Useful eg. for omitting rows containing column filters.
     *
     * @return The number of rows
     */
    public Integer getSkipRowsAfterColumnDefinitionRow();

    /**
     * Set the number of rows Xcelite should skip after
     * the row defining the annotated bean properties is parsed and
     * before the first data row is read.
     * Useful eg. for omitting rows containing column filters.
     *
     * @param skipRowsAfterColumnDefinitionRow The number of rows
     */
    public void setSkipRowsAfterColumnDefinitionRow (Integer skipRowsAfterColumnDefinitionRow);

    /**
     * Returns whether SheetReaders skip blank rows
     * or return empty objects for blank rows.
     * Default is SheetReaders skip blank rows
     *
     * @return Do we skip blank rows (Default: true)
     */
    public boolean isSkipBlankRows();

    /**
     * Determines whether SheetReaders skip blank rows
     * or return empty objects for blank rows
     *
     * @param skipBlankRows Should we skip blank rows
     */
    public void setSkipBlankRows(boolean skipBlankRows);
}

