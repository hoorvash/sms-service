package org.smartech.smartech.exception;

public class MyException extends Exception {

    protected int errorCode;

    public MyException(int errorCode) {this.errorCode = errorCode;}

    public MyException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public MyException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public MyException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
