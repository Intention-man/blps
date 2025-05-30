package com.example.prac.data.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ComplexRouteLegDTO {
    @NotBlank
    private String departureCity;

    @NotNull
    private LocalDate departureDate;

    @NotNull
    private LocalTime departureTimeStart;

    @NotNull
    private LocalTime departureTimeFinish;

    @NotBlank
    private String arrivalCity;

    @NotNull
    private LocalTime arrivalTimeStart;

    @NotNull
    private LocalTime arrivalTimeFinish;
}