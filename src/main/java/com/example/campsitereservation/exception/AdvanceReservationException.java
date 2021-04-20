package com.example.campsitereservation.exception;

public class AdvanceReservationException extends RuntimeException {

    public AdvanceReservationException() {
    }

    public AdvanceReservationException(String message) {
        super(message);
    }

    public AdvanceReservationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdvanceReservationException(Throwable cause) {
        super(cause);
    }

    public AdvanceReservationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
