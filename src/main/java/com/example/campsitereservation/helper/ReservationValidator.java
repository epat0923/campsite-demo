package com.example.campsitereservation.helper;

import com.example.campsitereservation.property.ReservationProperties;
import com.example.campsitereservation.dto.Reservation;
import com.example.campsitereservation.exception.AdvanceReservationException;
import com.example.campsitereservation.exception.InvalidateReservationException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class ReservationValidator {

    private final ReservationProperties properties;

    public ReservationValidator(ReservationProperties properties) {
        this.properties = properties;
    }

    public void validate(Reservation reservation) {
        validateDates(reservation.getArrivalDate(), reservation.getDepartureDate());
        validateDuration(reservation.getArrivalDate(), reservation.getDepartureDate());
        validateBooking(reservation.getArrivalDate());
    }

    public void validateDates(LocalDate arrivalDate, LocalDate departureDate) {
        if (departureDate.isBefore(arrivalDate) || arrivalDate.isAfter(departureDate)) {
            throw new InvalidateReservationException("Invalid arrival and or departure date.");
        }
    }

    private void validateDuration(LocalDate arrivalDate, LocalDate departureDate) {
        long duration = ChronoUnit.DAYS.between(arrivalDate, departureDate);
        if (duration > properties.getMaxReserveDays()) {
            throw new InvalidateReservationException("Exceeded maximum reservation limit of " + properties.getMaxReserveDays() + " days.");
        }
    }

    private void validateBooking(LocalDate arrivalDate) {
        LocalDateTime arrivalDataTime = arrivalDate.atStartOfDay();

        LocalDateTime fromTemp = LocalDateTime.from(LocalDateTime.now());
        long years = fromTemp.until(arrivalDataTime, ChronoUnit.YEARS);
        fromTemp = fromTemp.plusYears(years);

        long months = fromTemp.until(arrivalDataTime, ChronoUnit.MONTHS);
        fromTemp = fromTemp.plusMonths(months);

        long days = fromTemp.until(arrivalDataTime, ChronoUnit.DAYS);

        if (years > 0
                || months > properties.getMaxAdvanceBookingMonths()
                || (months == properties.getMaxAdvanceBookingMonths() && days > 0)
                || (months == 0 && days < properties.getMinAdvanceBookingDays())) {
            throw new AdvanceReservationException("Booking eligible for maximum of " +
                    properties.getMaxAdvanceBookingMonths() + " month in advance and minimum of " +
                    properties.getMinAdvanceBookingDays() + " day prior to arrival.");
        }
    }

    public boolean isSame(Reservation nowReservation, Reservation existedReservation) {
        return new EqualsBuilder()
                .append(nowReservation.getFullname(), existedReservation.getFullname())
                .append(nowReservation.getEmail(), existedReservation.getEmail())
                .append(nowReservation.getArrivalDate(), existedReservation.getArrivalDate())
                .append(nowReservation.getDepartureDate(), existedReservation.getDepartureDate())
                .isEquals();
    }
}
