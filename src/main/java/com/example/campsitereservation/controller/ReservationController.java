package com.example.campsitereservation.controller;

import com.example.campsitereservation.dto.Reservation;
import com.example.campsitereservation.service.ReservationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@RestController
@RequestMapping(path = "/campsite", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ReservationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(path = "/booking")
    public CompletableFuture<ResponseEntity<List<LocalDate>>> getAvailableDates(
            @RequestParam(value = "fromDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        LOGGER.info("Getting available dates. fromDate:{}, toDate:{}", fromDate, toDate);
        CompletableFuture<List<LocalDate>> dateList =
                reservationService.getAvailableDates(fromDate, defaultIfNull(toDate, fromDate.plusMonths(1)));
        return dateList.thenApply(localDates -> new ResponseEntity<List<LocalDate>>(localDates, HttpStatus.OK))
                .exceptionally(handleGetAvailableDatesFailure);
    }

    @PostMapping(path = "/booking",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> createReservation(@Valid @RequestBody Reservation reservation) {
        LOGGER.info("Creating Reservation. Reservation Payload:{}", reservation);
        String bookRefId = reservationService.createReservation(reservation);
        if (StringUtils.isNotBlank(bookRefId)) {
            return new ResponseEntity<>(bookRefId, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/booking/{bookRefId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reservation> updateReservation(@PathVariable(name = "bookRefId") String bookRefId,
                                                         @RequestBody @Valid Reservation reservation) {
        LOGGER.info("Updating Reservation. bookRefId:{}, Reservation Payload:{}", bookRefId, reservation);
        reservation.setBookRefId(bookRefId);
        Reservation updatedReservation = reservationService.updateReservation(reservation);
        return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
    }

    @DeleteMapping(path = "/booking/{bookRefId}")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable(name = "bookRefId") String bookRefId) {
        LOGGER.info("Cancelling Reservation. bookRefId:{}", bookRefId);
        Reservation reservation = reservationService.cancelReservationByRefId(bookRefId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    public static final Function<Throwable, ? extends ResponseEntity<List<LocalDate>>> handleGetAvailableDatesFailure = throwable -> {
        LOGGER.error("Failed to get available dates: {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

}
