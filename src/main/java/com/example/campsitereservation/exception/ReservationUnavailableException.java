package com.example.campsitereservation.exception;

public class ReservationUnavailableException extends RuntimeException {

    public ReservationUnavailableException() {
    }

    public ReservationUnavailableException(String message) {
        super(message);
    }

    public ReservationUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationUnavailableException(Throwable cause) {
        super(cause);
    }

    public ReservationUnavailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
