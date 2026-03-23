package com.example.helmes_challenge.benefit.service;

import com.example.helmes_challenge.benefit.ParentalBenefit;
import com.example.helmes_challenge.benefit.ParentalBenefitRepository;
import com.example.helmes_challenge.benefit.dto.CalculationRequest;
import com.example.helmes_challenge.benefit.dto.CalculationResult;
import com.example.helmes_challenge.benefit.dto.ParentalBenefitApplicationResponse;
import com.example.helmes_challenge.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

    @Transactional(readOnly = true)
    public ParentalBenefitApplicationResponse getById(UUID id) {
        ParentalBenefit parentalBenefit = parentalBenefitRepository.findById(id)
                .orElseThrow(() -> new ApiException("Parental benefit not found", HttpStatus.NOT_FOUND));

        return toResponse(parentalBenefit);
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
