package com.example.helmes_challenge.benefit.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CalculationRequest(
        @NotNull
        @DecimalMin(value = "0.01", message = "Gross salary must be greater than zero")
        BigDecimal grossSalary,

        @NotNull
        @PastOrPresent(message = "Birth date cannot be in the future")
        LocalDate babyBirthDate
) {
}