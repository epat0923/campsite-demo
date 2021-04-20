package com.example.campsitereservation.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("reservation.config")
public class ReservationProperties {

    private Integer maxReserveDays = 1;
    private Integer minAdvanceBookingDays = 1;
    private Integer maxAdvanceBookingMonths = 1;

    public Integer getMaxReserveDays() {
        return maxReserveDays;
    }

    public void setMaxReserveDays(Integer maxReserveDays) {
        this.maxReserveDays = maxReserveDays;
    }

    public Integer getMinAdvanceBookingDays() {
        return minAdvanceBookingDays;
    }

    public void setMinAdvanceBookingDays(Integer minAdvanceBookingDays) {
        this.minAdvanceBookingDays = minAdvanceBookingDays;
    }

    public Integer getMaxAdvanceBookingMonths() {
        return maxAdvanceBookingMonths;
    }

    public void setMaxAdvanceBookingMonths(Integer maxAdvanceBookingMonths) {
        this.maxAdvanceBookingMonths = maxAdvanceBookingMonths;
    }
}
