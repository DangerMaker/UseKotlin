package com.ez08.trade.exception;

public class SessionLostException extends RuntimeException {

    public SessionLostException() {
        super();
    }

    public SessionLostException(String message) {
        super(message);
    }

    public SessionLostException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionLostException(Throwable cause) {
        super(cause);
    }
}
