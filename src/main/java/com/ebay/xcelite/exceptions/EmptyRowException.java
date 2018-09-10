package com.ebay.xcelite.exceptions;

/**
 * Exception that will be thrown when a whole row in the Excel sheet is empty
 * and the {@link com.ebay.xcelite.policies.MissingRowPolicy} is set
 * to THROW in {@link com.ebay.xcelite.options.XceliteOptions}.
 *
 * @author Johannes
 * @since 1.2
 */
public class EmptyRowException extends XceliteException {

    public EmptyRowException() {
        super("Empty Row encountered and MissingRowPolicy.THROW active");
    }

    public EmptyRowException(int rowNum) {
        super("Row "+rowNum + " was empty and MissingRowPolicy.THROW active");
    }


}