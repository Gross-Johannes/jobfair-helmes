package com.example.helmes_challenge.benefit.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ParentalBenefitApplicationResponse(
        UUID id,
        BigDecimal grossSalary,
        LocalDate babyBirthDate,
        CalculationResult calculationResult
) {
}
