package com.example.campsitereservation.service;

import com.example.campsitereservation.dto.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ReservationService {

    CompletableFuture<List<LocalDate>> getAvailableDates(LocalDate beginDate, LocalDate endDate);

    String createReservation(Reservation reservation);

    Reservation updateReservation(Reservation reservation);

    Reservation cancelReservationByRefId(String bookRefId);

}
