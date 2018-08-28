package com.ebay.xcelite.exceptions;

/**
 * @author Johannes
 */
public class EmptyRowException extends XceliteException {

    public EmptyRowException() {
        super("Empty Row encountered and MissingRowPolicy.THROW active");
    }

    public EmptyRowException(int idx) {
        super("Row "+idx + " was empty and MissingRowPolicy.THROW active");
    }


}