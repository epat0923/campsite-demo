package com.example.campsitereservation.repository;

import com.example.campsitereservation.domain.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Async
    CompletableFuture<List<ReservationEntity>> findAllByArrivalDateLessThanEqualAndDepartureDateGreaterThanEqual(LocalDate toDate, LocalDate fromData);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ReservationEntity r " +
            "WHERE (r.arrivalDate < :toDate) " +
            "AND (r.departureDate > :fromDate)")
    List<ReservationEntity> findAllBetweenDates(
            @Param("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Param("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ReservationEntity r " +
            "WHERE (r.arrivalDate < :toDate " +
            "AND r.departureDate > :fromDate) " +
            "AND r.bookRefId <> :bookReferenceId")
    List<ReservationEntity> findAllDatesExcept(
            @Param("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Param("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @Param("bookReferenceId") String bookReferenceId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ReservationEntity> findReservationEntityByBookRefId(String bookRefId);

}
