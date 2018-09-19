package com.ebay.xcelite.exceptions;

/**
 * Base class for exceptions that will be thrown when a row or cell
 * violates the currently valid {@link com.ebay.xcelite.policies.MissingRowPolicy}
 * or {@link com.ebay.xcelite.policies.MissingCellPolicy}.
 *
 * @author Johannes
 * @since 1.2
 */
public class PolicyViolationException extends XceliteException{

    public PolicyViolationException(String message) {
        super(message);
    }

    public PolicyViolationException(Exception exception) {
        super(exception);
    }

    public PolicyViolationException(String message, Exception exception) {
        super(message, exception);
    }
}
