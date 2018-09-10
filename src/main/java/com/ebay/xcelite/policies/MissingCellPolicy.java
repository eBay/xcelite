package com.ebay.xcelite.policies;

import org.apache.poi.ss.usermodel.Row;

/**
 * Used to specify the different possible policies to handle null and blank cells
 *
 * Modeled after the POI missing-cell policy
 *
 * @see org.apache.poi.ss.usermodel.Row.MissingCellPolicy
 * @see MissingRowPolicy
 */
public enum MissingCellPolicy {
    RETURN_NULL_AND_BLANK,
    RETURN_BLANK_AS_NULL,
    THROW;

    MissingCellPolicy() {}

    public static Row.MissingCellPolicy toPoiMissingCellPolicy (MissingCellPolicy policy) {
        if (policy.equals(MissingCellPolicy.THROW))
            return null;
        return Row.MissingCellPolicy.valueOf(policy.name());
    }
}