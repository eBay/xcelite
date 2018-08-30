package com.ebay.xcelite.exceptions;

/**
 * @author Johannes
 */
public class EmptyCellException extends XceliteException {

    public EmptyCellException(String cellName) {
        super("Column "+cellName + " was empty and MissingCellPolicy.THROW active");
    }


}