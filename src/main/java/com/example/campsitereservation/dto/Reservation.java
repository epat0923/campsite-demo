package com.example.campsitereservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@JsonInclude(Include.NON_NULL)
public class Reservation implements Serializable {

    private static final long serialVersionUID = -8745855038558306661L;

    private String bookRefId;

    @NotBlank(message = "Fullname cannot be blank")
    private String fullname;

    @NotBlank(message = "Email address cannot be blank")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotNull(message = "arrival date cannot be null")
    private LocalDate arrivalDate;

    @NotNull(message = "departure Date cannot be null")
    private LocalDate departureDate;

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

        Reservation that = (Reservation) o;

        return new EqualsBuilder()
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
                .append(bookRefId)
                .append(fullname)
                .append(email)
                .append(arrivalDate)
                .append(departureDate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("bookRefId", bookRefId)
                .append("fullname", fullname)
                .append("email", email)
                .append("arrivalDate", arrivalDate)
                .append("departureDate", departureDate)
                .toString();
    }
}
