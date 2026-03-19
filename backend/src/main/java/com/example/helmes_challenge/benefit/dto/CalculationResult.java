package com.example.helmes_challenge.benefit.dto;

import java.math.BigDecimal;
import java.util.List;

public record CalculationResult(
        BigDecimal monthlyEligibleSalary,
        BigDecimal dailyRate,
        BigDecimal totalPayment,
        List<MonthlyBenefitBreakdown> breakdown
) {
}