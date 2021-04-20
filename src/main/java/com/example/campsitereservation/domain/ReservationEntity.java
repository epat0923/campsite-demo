package com.example.campsitereservation.domain;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "reservation")
public class ReservationEntity implements Serializable {

    private static final long serialVersionUID = -3005110453428520704L;

    @Id
    @SequenceGenerator(
            name = "reserve_sequence",
            sequenceName = "reserve_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reserve_sequence"
    )
    private Long reservationId;

    private String bookRefId;
    private String fullname;
    private String email;
    private LocalDate arrivalDate;
    private LocalDate departureDate;


    public ReservationEntity() {
    }

    public ReservationEntity(String bookRefId, String fullname, String email, LocalDate arrivalDate, LocalDate departureDate) {
        this.bookRefId = bookRefId;
        this.fullname = fullname;
        this.email = email;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getBookRefId() {
        return bookRefId;
    }

    public void setBookRefId(String bookRefId) {
        this.bookRefId = bookRefId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ReservationEntity that = (ReservationEntity) o;

        return new EqualsBuilder()
                .append(reservationId, that.reservationId)
                .append(bookRefId, that.bookRefId)
                .append(fullname, that.fullname)
                .append(email, that.email)
                .append(arrivalDate, that.arrivalDate)
                .append(departureDate, that.departureDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(reservationId).append(bookRefId)
                .append(fullname).append(email)
                .append(arrivalDate).append(departureDate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("reservationId", reservationId)
                .append("bookRefId", bookRefId)
                .append("fullname", fullname)
                .append("email", email)
                .append("arrivalDate", arrivalDate)
                .append("departureDate", departureDate)
                .toString();
    }
}
