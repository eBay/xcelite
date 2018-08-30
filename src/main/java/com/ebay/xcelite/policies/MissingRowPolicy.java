package com.ebay.xcelite.policies;

/**
 * Used to specify the different possible policies for the case of null and blank rows
 *
 * Modeled after the POI missing-cell policy
 *
 */
public enum MissingRowPolicy {
    /** SheetReaders return null for blank rows **/
    RETURN_NULL,
    /** SheetReaders return empty objects for blank rows **/
    RETURN_EMPTY_OBJECT,
    /** SheetReaders skip blank rows **/
    SKIP,
    /** SheetReaders throw a
     * {@link com.ebay.xcelite.exceptions.EmptyRowException} for blank rows
     **/
    THROW;

    private MissingRowPolicy() {
    }

}