package com.example.helmes_challenge.benefit.service;

import com.example.helmes_challenge.benefit.ParentalBenefit;
import com.example.helmes_challenge.benefit.ParentalBenefitRepository;
import com.example.helmes_challenge.benefit.dto.CalculationRequest;
import com.example.helmes_challenge.benefit.dto.CalculationResult;
import com.example.helmes_challenge.benefit.dto.ParentalBenefitApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParentalBenefitApplicationService {
    private final ParentalBenefitCalculatorService calculatorService;
    private final ParentalBenefitRepository parentalBenefitRepository;

    public CalculationResult calculate(CalculationRequest request) {
        return calculatorService.calculate(request);
    }

    @Transactional
    public ParentalBenefitApplicationResponse create(CalculationRequest request) {
        ParentalBenefit parentalBenefit = ParentalBenefit.builder()
                .grossSalary(request.grossSalary())
                .babyBirthDate(request.babyBirthDate())
                .build();

        ParentalBenefit saved = parentalBenefitRepository.save(parentalBenefit);

        return toResponse(saved);
    }

    private ParentalBenefitApplicationResponse toResponse(ParentalBenefit parentalBenefit) {
        CalculationRequest calculationRequest = new CalculationRequest(
                parentalBenefit.getGrossSalary(),
                parentalBenefit.getBabyBirthDate()
        );

        CalculationResult calculation = calculatorService.calculate(calculationRequest);

        return new ParentalBenefitApplicationResponse(
                parentalBenefit.getId(),
                parentalBenefit.getGrossSalary(),
                parentalBenefit.getBabyBirthDate(),
                calculation
        );
    }
}
