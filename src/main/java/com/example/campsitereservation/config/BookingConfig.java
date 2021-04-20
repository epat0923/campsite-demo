package com.example.campsitereservation.config;

import com.example.campsitereservation.domain.ReservationEntity;
import com.example.campsitereservation.repository.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class BookingConfig {

    @Bean
    CommandLineRunner commandLineRunner(ReservationRepository repository) {
        return args -> {
            ReservationEntity john = new ReservationEntity(
                    "hwoi3u1t9y5i",
                    "John Warden",
                    "john.warden@gmail.com",
                    LocalDate.of(2021, Month.APRIL, 23),
                    LocalDate.of(2021, Month.APRIL, 25)
            );

            ReservationEntity mariam = new ReservationEntity(
                    "v5iuy4opw54y",
                    "Mariam Jamal",
                    "mariam.jamal@gmail.com",
                    LocalDate.of(2021, Month.APRIL, 26),
                    LocalDate.of(2021, Month.APRIL, 28)
            );

            repository.saveAll(
                    List.of(john, mariam)
            );
        };
    }
}
