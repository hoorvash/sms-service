package org.smartech.smartech.exception;

public class ServiceCallException extends MyException {

    public ServiceCallException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public ServiceCallException(String errorMessage, int errorCode) {
        super(errorMessage, errorCode);
    }
}
