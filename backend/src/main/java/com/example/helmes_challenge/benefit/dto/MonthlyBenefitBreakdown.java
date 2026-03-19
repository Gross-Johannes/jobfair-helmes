package com.example.helmes_challenge.benefit.dto;

import java.math.BigDecimal;

public record MonthlyBenefitBreakdown(
        int year,
        int month,
        int payableDays,
        BigDecimal paymentAmount
) {
}