package com.example.helmes_challenge.benefit.service;

import com.example.helmes_challenge.benefit.dto.CalculationRequest;
import com.example.helmes_challenge.benefit.dto.CalculationResult;
import com.example.helmes_challenge.benefit.dto.MonthlyBenefitBreakdown;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParentalBenefitCalculatorService {
    private static final BigDecimal MONTHLY_CAP = BigDecimal.valueOf(4000);
    private static final BigDecimal DAILY_DIVISOR = BigDecimal.valueOf(30);

    public CalculationResult calculate(CalculationRequest request) {
        LocalDate birthDate = request.babyBirthDate();
        BigDecimal monthlyEligibleSalary = request.grossSalary().min(MONTHLY_CAP).setScale(2, RoundingMode.HALF_UP);
        BigDecimal dailyRate = monthlyEligibleSalary.divide(DAILY_DIVISOR, 8, RoundingMode.HALF_UP);
        BigDecimal totalPayment = BigDecimal.ZERO;

        List<MonthlyBenefitBreakdown> breakdown = new ArrayList<>();

        for (int monthIndex = 0; monthIndex < 12; monthIndex++) {
            YearMonth currentMonth = YearMonth.from(birthDate).plusMonths(monthIndex);
            int payableDays = currentMonth.lengthOfMonth();

            if (monthIndex == 0) {
                payableDays = currentMonth.lengthOfMonth() - birthDate.getDayOfMonth() + 1;
            }

            BigDecimal monthlyBenefit = dailyRate
                    .multiply(BigDecimal.valueOf(payableDays))
                    .setScale(2, RoundingMode.HALF_UP);

            totalPayment = totalPayment.add(monthlyBenefit);

            breakdown.add(new MonthlyBenefitBreakdown(
                    currentMonth.getYear(),
                    currentMonth.getMonthValue(),
                    payableDays,
                    monthlyBenefit
            ));
        }

        return new CalculationResult(
                monthlyEligibleSalary,
                dailyRate.setScale(2, RoundingMode.HALF_UP),
                totalPayment.setScale(2, RoundingMode.HALF_UP),
                breakdown
        );
    }
}
