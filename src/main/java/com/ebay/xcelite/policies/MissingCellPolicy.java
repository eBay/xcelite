package com.ebay.xcelite.policies;

import org.apache.poi.ss.usermodel.Row;

/**
 * Used to specify the different possible policies for the case of null and blank cells
 *
 * Modeled after the POI missing-cell policy
 *
 * @see org.apache.poi.ss.usermodel.Row.MissingCellPolicy
 */
public enum MissingCellPolicy {
    RETURN_NULL_AND_BLANK,
    RETURN_BLANK_AS_NULL,
    THROW;

    private MissingCellPolicy() {}

    public static Row.MissingCellPolicy toPoiMissingCellPolicy (MissingCellPolicy policy) {
        if (policy.equals(MissingCellPolicy.THROW))
            return null;
        return Row.MissingCellPolicy.valueOf(policy.name());
    }
}