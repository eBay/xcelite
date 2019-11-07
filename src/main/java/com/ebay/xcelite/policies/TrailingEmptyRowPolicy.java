package com.ebay.xcelite.policies;

/**
 * Used to specify the different possible policies to handle null and
 * trailing blank rows following data blocks for readers and for writers,
 * null objects at the end of the input object collection.
 *
 * Default is SKIP.
 *
 * This is different from {@link MissingRowPolicy} which deals with
 * empty/missing rows in the table or null objects in the data.
 *
 * @see MissingRowPolicy
 * @see MissingCellPolicy
 * @since 1.2
 */
public enum TrailingEmptyRowPolicy {
    /**
     * SheetReaders return null objects for blank rows after the data block,
     * SheetWriters write a null row (row gets created, but no
     * cells in it) for null objects at the end of the object collection.
     * Note: for SheetWriters, it cannot write null rows for empty objects
     * as there is no general way to determine whether an object is empty
     **/
    NULL,

    /**
     * SheetReaders return empty objects for blank rows after the data block,
     * SheetWriters write a line with empty cells for empty and null objects
     * at the end of the object collection.
     **/
    EMPTY_OBJECT,

    /**
     * SheetReaders skip blank rows after the data block,
     * SheetWriters write nothing for null objects at the end of the object
     * collection. Note: for SheetWriters, it cannot filter out empty objects
     * as there is no general way to determine whether an object is empty
     **/
    SKIP,

    /**
     * SheetReaders throw a
     * {@link com.ebay.xcelite.exceptions.EmptyRowException} for blank rows
     * after the data block,
     * SheetWriters throw a {@link com.ebay.xcelite.exceptions.PolicyViolationException}
     * for null objects at the end of the object collection.
     * Note: for SheetWriters, it cannot throw for empty objects as there is no
     * general way to determine whether an object is empty
     **/
    THROW;

}