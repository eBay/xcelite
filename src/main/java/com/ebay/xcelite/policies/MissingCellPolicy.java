package com.ebay.xcelite.policies;

import org.apache.poi.ss.usermodel.Row;

/**
 * Used to specify the different possible policies to handle null and blank cells.
 *
 * Loosely modeled after the POI missing-cell policy.
 *
 * @see org.apache.poi.ss.usermodel.Row.MissingCellPolicy
 * @see MissingRowPolicy
 * @since 1.2
 */
public enum MissingCellPolicy {
    /**     *
     * Readers: Empty String formatted cells will be returned as "", the
     * zero-length empty string, other empty cells where matching the Java class
     * does not allow for emptiness will be returned as null
     *
     * Writers: Empty string member variables will be written as empty cells, null
     * member variables will be written as null.
     */
    RETURN_NULL_AND_BLANK,

    /**
     * Readers: All empty cells will be returned as null objects.
     *
     * Writers: Empty string member variables will be written as null cells, null
     * member variables will also be written as null.
     */
    RETURN_BLANK_AS_NULL,

    /**
     * Readers: Xcelite will throw a {@link com.ebay.xcelite.exceptions.EmptyCellException} if
     * it encounters an empty cell in the spreadsheet.
     *
     * Writers: Xcelite will throw a {@link com.ebay.xcelite.exceptions.PolicyViolationException} if
     * it encounters a null member variable.
     */
    THROW;

    public static Row.MissingCellPolicy toPoiMissingCellPolicy (MissingCellPolicy policy) {
        if (policy.equals(MissingCellPolicy.THROW))
            return null;
        return Row.MissingCellPolicy.valueOf(policy.name());
    }
}