package com.example.campsitereservation.exception;

import com.example.campsitereservation.domain.ServiceError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidateReservationException.class)
    protected ResponseEntity<Object> handleInvalidReservation(InvalidateReservationException irx) {
        return buildResponseEntity(new ServiceError(BAD_REQUEST, "Invalid reservation details", irx));
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundReservation(ReservationNotFoundException rnx) {
        return buildResponseEntity(new ServiceError(BAD_REQUEST, "Reservation not found", rnx));
    }

    @ExceptionHandler(AdvanceReservationException.class)
    protected ResponseEntity<Object> handleAdvanceReservationError(AdvanceReservationException arx) {
        return buildResponseEntity(new ServiceError(BAD_REQUEST, "Violate advance reservation rule", arx));
    }

    @ExceptionHandler(ReservationUnavailableException.class)
    protected ResponseEntity<Object> handleUnavailableReservation(ReservationUnavailableException rux) {
        return buildResponseEntity(new ServiceError(BAD_REQUEST, "Reservation unavailable", rux));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request)
    {
        List<String> details = ex.getConstraintViolations()
                .parallelStream()
                .map(e -> e.getMessage())
                .collect(Collectors.toList());

        return buildResponseEntity(new ServiceError(BAD_REQUEST, details.get(0), ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ServiceError(BAD_REQUEST, "Validation error", ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ServiceError serviceError) {
        return new ResponseEntity<>(serviceError, serviceError.getStatus());
    }
}
