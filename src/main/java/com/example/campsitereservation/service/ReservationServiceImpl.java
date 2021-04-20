package com.example.campsitereservation.service;

import com.example.campsitereservation.domain.ReservationEntity;
import com.example.campsitereservation.dto.Reservation;
import com.example.campsitereservation.exception.ReservationNotFoundException;
import com.example.campsitereservation.exception.ReservationUnavailableException;
import com.example.campsitereservation.helper.ReservationValidator;
import com.example.campsitereservation.repository.ReservationRepository;
import com.example.campsitereservation.util.ReferenceIdUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@Service
public class ReservationServiceImpl implements ReservationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationValidator validator;
    private final ReservationRepository repository;
    private final ModelMapper modelMapper;
    private final ReentrantLock lock;


    public ReservationServiceImpl(ReservationValidator validator, ReservationRepository repository, ModelMapper modelMapper) {
        this.validator = validator;
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.lock = new ReentrantLock();
    }

    @Async("asyncTaskExecutor")
    @Override
    public CompletableFuture<List<LocalDate>> getAvailableDates(final LocalDate fromDate, final LocalDate toDate) {
        LOGGER.info("Start processing getAvailableDates");
        validator.validateDates(fromDate, toDate);
        return getExistedReservation(fromDate, toDate);
    }

    @Override
    @Transactional(isolation = READ_COMMITTED)
    public String createReservation(Reservation reservation) {
        validator.validate(reservation);
        try {
            lock.lock();
            return verifyAndSave(reservation);
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional(isolation = READ_COMMITTED)
    public Reservation updateReservation(Reservation reservation) {
        validator.validate(reservation);
        try {
            lock.lock();
            return updateReservationEntityFromReservation(reservation);
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional(isolation = READ_COMMITTED)
    public Reservation cancelReservationByRefId(String bookRefId) {
        try {
            lock.lock();
            ReservationEntity reservationEntity = repository.findReservationEntityByBookRefId(bookRefId)
                    .orElseThrow(() -> {
                        throw new ReservationNotFoundException("Booking Reference Id: [" + bookRefId + "] not found");
                    });
            repository.delete(reservationEntity);
            return modelMapper.map(reservationEntity, Reservation.class);
        } finally {
            lock.unlock();
        }
    }

    private String verifyAndSave(final Reservation reservation) {
        LOGGER.debug("{} - verifyAndSave", Thread.currentThread().getName());
        List<ReservationEntity> reservationList =
                repository.findAllBetweenDates(reservation.getArrivalDate(), reservation.getDepartureDate());
        if (!CollectionUtils.isEmpty(reservationList)) {
            throw new ReservationUnavailableException("Reservation between [" + reservation.getArrivalDate() + "] and ["
                    + reservation.getDepartureDate() + "] is not available. Please select a new date range and retry.");
        }

        ReservationEntity entity = repository.save(createReservationEntity(reservation));
        return entity != null ? entity.getBookRefId() : null;
    }

    private Reservation updateReservationEntityFromReservation(final Reservation reservation) {
        LOGGER.info("{} - updateReservationEntityFromReservation", Thread.currentThread().getName());
        ReservationEntity resEntity = repository.findReservationEntityByBookRefId(reservation.getBookRefId())
                .orElseThrow(() -> {
                    throw new ReservationNotFoundException("Booking Reference Id: [" + reservation.getBookRefId() + "] not found");
                });

        Reservation existedReservation = modelMapper.map(resEntity, Reservation.class);
        if (validator.isSame(reservation, existedReservation)) {
            // no updates if no change in reservation
            return reservation;
        }

        if (!Objects.equals(resEntity.getFullname(), reservation.getFullname())) {
            resEntity.setFullname(reservation.getFullname());
        }

        if (!Objects.equals(resEntity.getEmail(), reservation.getEmail())) {
            resEntity.setEmail(reservation.getEmail());
        }

        boolean isArrivalChanged = !Objects.equals(resEntity.getArrivalDate(), reservation.getArrivalDate());
        boolean isDepartureChanged = !Objects.equals(resEntity.getDepartureDate(), reservation.getDepartureDate());

        if (isArrivalChanged || isDepartureChanged) {
            List<ReservationEntity> reservationList =
                    repository.findAllDatesExcept(reservation.getArrivalDate(), reservation.getDepartureDate(), reservation.getBookRefId());
            if (!CollectionUtils.isEmpty(reservationList)) {
                throw new ReservationUnavailableException("Reservation between [" + reservation.getArrivalDate() + "] and ["
                        + reservation.getDepartureDate() + "] is not available. Please select a new date range and retry.");
            }

            if (isArrivalChanged) {
                resEntity.setArrivalDate(reservation.getArrivalDate());
            }
            if (isDepartureChanged) {
                resEntity.setDepartureDate(reservation.getDepartureDate());
            }
        }

        resEntity = repository.save(resEntity);
        return modelMapper.map(resEntity, Reservation.class);
    }

    private ReservationEntity createReservationEntity(final Reservation reservation) {
        ReservationEntity entity = new ReservationEntity();
        entity.setBookRefId(ReferenceIdUtils.generateRefId(12));
        entity.setFullname(reservation.getFullname());
        entity.setEmail(reservation.getEmail());
        entity.setArrivalDate(reservation.getArrivalDate());
        entity.setDepartureDate(reservation.getDepartureDate());
        return entity;
    }

    private CompletableFuture<List<LocalDate>> getExistedReservation(final LocalDate fromDate, final LocalDate toDate) {
        LOGGER.info("Get existed reservation ...");
        return repository
                .findAllByArrivalDateLessThanEqualAndDepartureDateGreaterThanEqual(toDate, fromDate)
                .thenCompose(this::mapToReservation)
                .thenCompose(reservations -> {
                    return this.getVacantDates(fromDate, toDate, reservations);
                });
    }

    private CompletableFuture<List<Reservation>> mapToReservation(final List<ReservationEntity> reservationEntities) {
        List<Reservation> reservations = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reservationEntities)) {
            reservations = reservationEntities.stream()
                    .map(re -> modelMapper.map(re, Reservation.class))
                    .collect(Collectors.toList());
        }
        return CompletableFuture.completedFuture(reservations);
    }

    private CompletableFuture<List<LocalDate>> getVacantDates(final LocalDate fromDate, final LocalDate toDate, final List<Reservation> reservations) {
        LOGGER.info("In getVacantDates ...");
        Set<LocalDate> reservedDateList = new TreeSet<>();
        if (!CollectionUtils.isEmpty(reservations)) {
            List<LocalDate> dates;
            for (Reservation reservation : reservations) {
                dates = getDatesInRangeExclusive(reservation.getArrivalDate(), reservation.getDepartureDate());
                reservedDateList.addAll(dates);
            }
        }

        List<LocalDate> availableDates = filterReservedDates(reservedDateList, getDatesInRangeInclusive(fromDate, toDate));

        return CompletableFuture.completedFuture(availableDates);
    }

    private List<LocalDate> getDatesInRangeExclusive(final LocalDate fromDate, final LocalDate toDate) {
        List<LocalDate> dateList = fromDate.datesUntil(toDate).collect(Collectors.toList());
        if (dateList.isEmpty()) {
            dateList.add(fromDate);
        }
        return dateList;
    }

    private List<LocalDate> getDatesInRangeInclusive(final LocalDate fromDate, final LocalDate toDate) {
        List<LocalDate> dateList = fromDate.datesUntil(toDate).collect(Collectors.toList());
        dateList.add(toDate);
        return dateList;
    }

    private List<LocalDate> filterReservedDates(final Set<LocalDate> reservedDates, final List<LocalDate> datesInRange) {
        if (CollectionUtils.isEmpty(reservedDates)) {
            return datesInRange;
        }

        List<LocalDate> availableDates = new ArrayList<>();
        for (LocalDate calDate : datesInRange) {
            if (!reservedDates.contains(calDate)) {
                availableDates.add(calDate);
            }
        }
        return availableDates;
    }

}
