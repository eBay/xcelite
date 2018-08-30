package com.ebay.xcelite.options;

import com.ebay.xcelite.policies.MissingCellPolicy;
import com.ebay.xcelite.policies.MissingRowPolicy;

/**
 * Configuration class for setting options of a {@link com.ebay.xcelite.Xcelite}
 * object.
 *
 * @since 1.2
 */


public class XceliteOptions {

    private Integer skipRowsBeforeColDefinitionRow = 0;
    private Integer skipRowsAfterColDefinitionRow = 0;
    private MissingCellPolicy missingCellPolicy = MissingCellPolicy.RETURN_BLANK_AS_NULL;
    private MissingRowPolicy missingRowPolicy = MissingRowPolicy.SKIP;


    /**
     * Returns the number of rows Xcelite should skip before trying
     * to parse the next one as the row defining the annotated bean
     * properties
     *
     * @return The number of rows
     */
    public Integer getSkipRowsBeforeColumnDefinitionRow() {
        return skipRowsBeforeColDefinitionRow;
    }


    /**
     * Sets the number of rows Xcelite should skip before trying
     * to parse the next one as the row defining the annotated bean
     * properties
     *
     * @param skipRowsBeforeColumnDefinitionRow The number of rows to skip
     */
    public void setSkipRowsBeforeColumnDefinitionRow(Integer skipRowsBeforeColumnDefinitionRow) {
        this.skipRowsBeforeColDefinitionRow = skipRowsBeforeColumnDefinitionRow;
    }

    /**
     * Returns the number of rows Xcelite should skip after
     * the row defining the annotated bean properties is parsed and
     * before the first data row is read.
     * Useful eg. for omitting rows containing column filters.
     *
     * @return The number of rows
     */
    public Integer getSkipRowsAfterColumnDefinitionRow() {
        return skipRowsAfterColDefinitionRow;
    }

    /**
     * Set the number of rows Xcelite should skip after
     * the row defining the annotated bean properties is parsed and
     * before the first data row is read.
     * Useful eg. for omitting rows containing column filters.
     *
     * @param skipRowsAfterColumnDefinitionRow The number of rows
     */
    public void setSkipRowsAfterColumnDefinitionRow(Integer skipRowsAfterColumnDefinitionRow) {
        this.skipRowsAfterColDefinitionRow = skipRowsAfterColumnDefinitionRow;
    }

    /**
     * Used to specify the different possible policies for the case of null and blank cells
     * @return currently active policy for null and blank cells
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
     */
    public MissingRowPolicy getMissingRowPolicy() {
        return missingRowPolicy;
    }

    /**
     * Used to specify the different possible policies for the case of null and blank rows
     * @param missingRowPolicy set policy for null and blank rows
     */
    public void setMissingRowPolicy(MissingRowPolicy missingRowPolicy) {
        this.missingRowPolicy = missingRowPolicy;
    }

}
