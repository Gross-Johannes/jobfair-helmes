package com.example.helmes_challenge.benefit.service;

import com.example.helmes_challenge.benefit.dto.CalculationRequest;
import com.example.helmes_challenge.benefit.dto.CalculationResult;
import com.example.helmes_challenge.benefit.dto.MonthlyBenefitBreakdown;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ParentalBenefitCalculatorServiceTest {

    private final ParentalBenefitCalculatorService calculatorService = new ParentalBenefitCalculatorService();

    private static BigDecimal sumOfMonthlyPayments(CalculationResult result) {
        return result.breakdown().stream()
                .map(MonthlyBenefitBreakdown::paymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Test
    void calculate_returnsExpectedResult_forMidMonthBirthDate() {
        CalculationRequest request = new CalculationRequest(new BigDecimal("3000.00"), LocalDate.of(2026, 3, 10));

        CalculationResult result = calculatorService.calculate(request);

        assertThat(result.monthlyEligibleSalary()).isEqualByComparingTo("3000.00");
        assertThat(result.dailyRate()).isEqualByComparingTo("100.00");
        assertThat(result.totalPayment()).isEqualByComparingTo("35600.00");
        assertThat(result.breakdown()).hasSize(12);

        MonthlyBenefitBreakdown firstMonth = result.breakdown().getFirst();
        assertThat(firstMonth.year()).isEqualTo(2026);
        assertThat(firstMonth.month()).isEqualTo(3);
        assertThat(firstMonth.payableDays()).isEqualTo(22);
        assertThat(firstMonth.paymentAmount()).isEqualByComparingTo("2200.00");

        assertThat(sumOfMonthlyPayments(result)).isEqualByComparingTo(result.totalPayment());
    }

    @Test
    void calculate_appliesMonthlyCapAndRounding() {
        CalculationRequest request = new CalculationRequest(new BigDecimal("5000.00"), LocalDate.of(2026, 1, 1));

        CalculationResult result = calculatorService.calculate(request);

        assertThat(result.monthlyEligibleSalary()).isEqualByComparingTo("4000.00");
        assertThat(result.dailyRate()).isEqualByComparingTo("133.33");
        assertThat(result.totalPayment()).isEqualByComparingTo("48666.64");
        assertThat(result.breakdown()).hasSize(12);
        assertThat(sumOfMonthlyPayments(result)).isEqualByComparingTo(result.totalPayment());
    }

    @Test
    void calculate_handlesMonthEndBirthDateInFirstMonth() {
        CalculationRequest request = new CalculationRequest(new BigDecimal("3600.00"), LocalDate.of(2026, 5, 31));

        CalculationResult result = calculatorService.calculate(request);

        MonthlyBenefitBreakdown firstMonth = result.breakdown().getFirst();
        assertThat(firstMonth.year()).isEqualTo(2026);
        assertThat(firstMonth.month()).isEqualTo(5);
        assertThat(firstMonth.payableDays()).isEqualTo(1);
        assertThat(firstMonth.paymentAmount()).isEqualByComparingTo("120.00");

        assertThat(result.totalPayment()).isEqualByComparingTo("40200.00");
        assertThat(sumOfMonthlyPayments(result)).isEqualByComparingTo(result.totalPayment());
    }
}
