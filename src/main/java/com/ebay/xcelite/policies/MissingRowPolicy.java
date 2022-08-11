package com.ebay.xcelite.policies;

/**
 * Used to specify the different possible policies to handle null and
 * blank rows in data blocks for readers and null objects for writers.
 *
 * Default is NULL.
 *
 * This is different from {@link TrailingEmptyRowPolicy} which deals with
 * empty/missing rows *after* the data block.
 *
 * Loosely modeled after {@link MissingCellPolicy}.
 *
 * @see MissingCellPolicy
 * @see TrailingEmptyRowPolicy
 * @since 1.2
 */
public enum MissingRowPolicy {
    /**
     * SheetReaders return null for blank rows,
     * SheetWriters write a null row (row gets created, but no
     * cells in it) for null objects
     **/
    NULL,
    /**
     * SheetReaders return empty objects for blank rows,
     * SheetWriters write a line with empty cells for null objects
     **/
    EMPTY_OBJECT,
    /**
     * SheetReaders skip blank rows,
     * SheetWriters write nothing for null objects
     **/
    SKIP,
    /**
     * SheetReaders throw a
     * {@link com.ebay.xcelite.exceptions.EmptyRowException} for blank rows,
     * SheetWriters throw a {@link com.ebay.xcelite.exceptions.PolicyViolationException}
     * for null objects
     **/
    THROW;

}