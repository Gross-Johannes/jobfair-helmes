package com.example.helmes_challenge.benefit.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CalculationRequest(
        @NotNull
        @DecimalMin(value = "0.01", message = "Gross salary must be greater than zero")
        BigDecimal grossSalary,

        @NotNull
        LocalDate babyBirthDate
) {
}