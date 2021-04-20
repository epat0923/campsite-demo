package com.example.campsitereservation.exception;

public class InvalidateReservationException extends RuntimeException {

    public InvalidateReservationException() {
        super();
    }

    public InvalidateReservationException(String message) {
        super(message);
    }

    public InvalidateReservationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidateReservationException(Throwable cause) {
        super(cause);
    }

    protected InvalidateReservationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
