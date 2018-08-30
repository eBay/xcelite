package com.ebay.xcelite.exceptions;

/**
 * @author Johannes
 */
public class EmptyCellException extends XceliteException {

    public EmptyCellException() {
        super("Cell was empty and MissingCellPolicy.THROW active");
    }

    public EmptyCellException(String cellName) {
        super("Cell of column "+cellName + " was empty and MissingCellPolicy.THROW active");
    }
}