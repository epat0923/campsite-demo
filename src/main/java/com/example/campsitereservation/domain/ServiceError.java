package com.example.campsitereservation.domain;

import org.springframework.http.HttpStatus;

public class ServiceError {

    private HttpStatus status;
    private String errorMessage;
    private String errorReason;

    private ServiceError() {
    }

    public ServiceError(HttpStatus status) {
        this();
        this.status = status;
    }

    public ServiceError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.errorMessage = "Unexpected error";
        this.errorReason = ex.getLocalizedMessage();
    }

    public ServiceError(HttpStatus status, String errorMessage, Throwable ex) {
        this();
        this.status = status;
        this.errorMessage = errorMessage;
        this.errorReason = ex.getLocalizedMessage();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
}
