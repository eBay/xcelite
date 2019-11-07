package com.ebay.xcelite.options;

import com.ebay.xcelite.policies.MissingCellPolicy;
import com.ebay.xcelite.policies.MissingRowPolicy;
import com.ebay.xcelite.policies.TrailingEmptyRowPolicy;
import lombok.EqualsAndHashCode;

/**
 * Configuration class for setting options of a {@link com.ebay.xcelite.Xcelite}
 * object and sheet readers/writers.
 *
 * @author Johannes
 * @since 1.2
 */

@EqualsAndHashCode
public class XceliteOptions {
    private Boolean hasHeaderRow = null;
    private boolean headerParsingIsCaseSensitive = true;
    private Integer headerRowIndex = 0;
    private Integer firstDataRowIndex = 0;
    private MissingCellPolicy missingCellPolicy = MissingCellPolicy.RETURN_BLANK_AS_NULL;
    private MissingRowPolicy missingRowPolicy = MissingRowPolicy.NULL;
    private TrailingEmptyRowPolicy trailingEmptyRowPolicy = TrailingEmptyRowPolicy.SKIP;
    private boolean anyColumnCreatesCollection = false;

    public XceliteOptions() {}

    /**
     * Copy constructor
     */
    public XceliteOptions(XceliteOptions other) {
        this.hasHeaderRow = other.hasHeaderRow;
        this.headerParsingIsCaseSensitive = other.headerParsingIsCaseSensitive;
        this.headerRowIndex = other.headerRowIndex;
        this.firstDataRowIndex = other.firstDataRowIndex;
        this.missingCellPolicy = other.missingCellPolicy;
        this.missingRowPolicy = other.missingRowPolicy;
        this.trailingEmptyRowPolicy = other.trailingEmptyRowPolicy;
        this.anyColumnCreatesCollection = other.anyColumnCreatesCollection;
    }

    /**
     * Returns the row index of the row defining the header names
     * (which get translated into annotated bean properties).
     * This index is zero-based, so `headerRowIndex`-value of 3
     * creates 3 empty rows before the header row.
     *
     * @return column index of the header row.
     * @since 1.2
     */
    public Integer getHeaderRowIndex() {
        return headerRowIndex;
    }


    /**
     * Sets the row index of the row defining the header names (
     * which get translated into annotated bean properties).
     * This index is zero-based.
     *
     * @param headerRowIndex column index of the header row
     * @since 1.2
     */
    public void setHeaderRowIndex(Integer headerRowIndex) {
        this.headerRowIndex = headerRowIndex;
    }

    /**
     * Returns row index of first data row. This index is zero-based.
     * Useful eg. for omitting rows containing column filters.
     *
     * @return The column index of the first data row.
     * @since 1.2
     */
    public Integer getFirstDataRowIndex() {
        return firstDataRowIndex;
    }

    /**
     * Set row index of first data row. This index is zero-based.
     * Useful eg. for omitting rows containing column filters.
     *
     * @param firstDataRowIndex column index of the first data row
     * @since 1.2
     */
    public void setFirstDataRowIndex(Integer firstDataRowIndex) {
        this.firstDataRowIndex = firstDataRowIndex;
    }

    /**
     * Used to specify the different possible policies for the case of null and blank cells
     * @return currently active policy for null and blank cells
     *
     * @since 1.2
     */
    public MissingCellPolicy getMissingCellPolicy() {
        return missingCellPolicy;
    }

    /**
     * Used to specify the different possible policies for the case of null and blank cells
     * @param missingCellPolicy set policy for null and blank cells
     */
    public void setMissingCellPolicy(MissingCellPolicy missingCellPolicy) {
        this.missingCellPolicy = missingCellPolicy;
    }

    /**
     * Used to specify the different possible policies for the case of null and blank rows
     * @return currently active policy for null and blank rows
     *
     * @since 1.2
     */
    public MissingRowPolicy getMissingRowPolicy() {
        return missingRowPolicy;
    }

    /**
     * Used to specify the different possible policies for the case of null and blank rows
     * @param missingRowPolicy set policy for null and blank rows
     *
     * @since 1.2
     */
    public void setMissingRowPolicy(MissingRowPolicy missingRowPolicy) {
        this.missingRowPolicy = missingRowPolicy;
    }

    /**
     * Readers: whether to expect a header row.
     *
     * Writers: Specifies whether to generate a row with header columns that can be mapped
     * to {@link com.ebay.xcelite.annotations.Column} annotations.
     *
     * {@link com.ebay.xcelite.reader.BeanSheetReader} needs this set to true,
     * {@link com.ebay.xcelite.reader.SimpleSheetReader} to false.
     *
     * Default: null, the type of Reader/Writer determines whether
     * a header row is expected.
     *
     * @since 1.2
     * @return whether to generate a column definition row
     */
    public Boolean isHasHeaderRow() {
        return hasHeaderRow;
    }

    /**
     * Readers: whether to expect a header row.
     *
     * Writers: Specifies whether to generate a row with header columns that can be mapped
     * to {@link com.ebay.xcelite.annotations.Column} annotations.
     *
     * {@link com.ebay.xcelite.reader.BeanSheetReader} needs this set to true,
     * {@link com.ebay.xcelite.reader.SimpleSheetReader} to false.
     *
     * @since 1.2
     * @param hasHeaderRow whether to generate a column definition row
     */
    public void setHasHeaderRow(boolean hasHeaderRow) {
        this.hasHeaderRow = hasHeaderRow;
    }

    /**
     * Specifies whether the column headers in the column definition row will be matched
     * to {@link com.ebay.xcelite.annotations.Column} annotations case-sensitive or case-insensitive.
     * Version 1.0.x and 1.1.x always used case-sensitive parsing.
     *
     * @since 1.2
     * @return whether to parse column headers case-sensitive
     */
    public boolean isHeaderParsingIsCaseSensitive() {
        return headerParsingIsCaseSensitive;
    }

    /**
     * Specifies whether the column headers in the column definition row will be matched
     * to {@link com.ebay.xcelite.annotations.Column} annotations case-sensitive or case-insensitive.
     * Version 1.0.x and 1.1.x always used case-sensitive parsing.
     *
     * @since 1.2
     * @param headerParsingIsCaseSensitive whether to parse column headers case-sensitive
     */
    public void setHeaderParsingIsCaseSensitive(boolean headerParsingIsCaseSensitive) {
        this.headerParsingIsCaseSensitive = headerParsingIsCaseSensitive;
    }

    /**
     * Used to specify the policy to handle null and trailing blank rows following
     * data blocks for readers and trailing null objects for writers. Note: for
     * SheetWriters, it cannot filter out empty objects if set to SKIP
     *
     * @return trailingEmptyRowPolicy the policy for trailing null and blank rows
     *
     * @since 1.2
     */
    public TrailingEmptyRowPolicy getTrailingEmptyRowPolicy() {
        return trailingEmptyRowPolicy;
    }

    /**
     * Used to specify the policy to handle null and trailing blank rows following
     * data blocks for readers and trailing null objects for writers. Note: for
     * SheetWriters, it cannot filter out empty objects if set to SKIP
     *
     * @param trailingEmptyRowPolicy set policy for trailing null and blank rows
     *
     * @since 1.2
     */
    public void setTrailingEmptyRowPolicy(TrailingEmptyRowPolicy trailingEmptyRowPolicy) {
        this.trailingEmptyRowPolicy = trailingEmptyRowPolicy;
    }

    /**
     * Used to specify whether a `@AnyColumn` annotation creates a {@link java.util.Collection}
     * or a single object. In the latter case, multiple Excel columns *with the same name*
     * will overwrite each other's value objects on reading. Setting this to `true` will
     * always return a Collection for each column key.
     *
     * Default behavior is to create an object to stay compatible with the original version
     * but this will change in version 2 where the default will switch to returning a
     * collection.
     *
     * This setting has no influence on {@link com.ebay.xcelite.writer.SheetWriter}s or
     * {@link com.ebay.xcelite.reader.SimpleSheetReader}s
     *
     * @return whether a `@AnyColumn` annotation creates a {@link java.util.Collection}
     * or a single object
     *
     * @since 1.3
     */
    public boolean isAnyColumnCreatesCollection() {
        return anyColumnCreatesCollection;
    }

    /**
     * Used to specify whether a `@AnyColumn` annotation creates a {@link java.util.Collection}
     * or a single object. In the latter case, multiple Excel columns *with the same name*
     * will overwrite each other's value objects on reading. Setting this to `true` will
     * always return a Collection for each column key.
     *
     * Default behavior is to create an object to stay compatible with the original version
     * but this will change in version 2 where the default will switch to returning a
     * collection.
     *
     * This setting has no influence on {@link com.ebay.xcelite.writer.SheetWriter}s or
     * {@link com.ebay.xcelite.reader.SimpleSheetReader}s
     *
     * @param anyColumnCreatesCollection true: a `@AnyColumn` annotation creates a
     * {@link java.util.Collection}, false: a single object
     *
     * @since 1.3
     */
    public void setAnyColumnCreatesCollection(boolean anyColumnCreatesCollection) {
        this.anyColumnCreatesCollection = anyColumnCreatesCollection;
    }

}
