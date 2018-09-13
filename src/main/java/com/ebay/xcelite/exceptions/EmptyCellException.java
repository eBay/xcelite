package com.ebay.xcelite.exceptions;

/**
 * Exception that will be thrown when a cell in the Excel sheet is empty
 * and the {@link com.ebay.xcelite.policies.MissingCellPolicy} is set
 * to THROW in {@link com.ebay.xcelite.options.XceliteOptions}.
 *
 * @author Johannes
 * @since 1.2
 */
public class EmptyCellException extends XceliteException {

    public EmptyCellException() {
        super("Cell was empty and MissingCellPolicy.THROW active");
    }

    public EmptyCellException(String cellName) {
        super("Cell of column "+cellName + " was empty and MissingCellPolicy.THROW active");
    }
}