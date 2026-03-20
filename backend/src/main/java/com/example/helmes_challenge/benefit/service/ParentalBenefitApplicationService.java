package com.example.helmes_challenge.benefit.service;

import com.example.helmes_challenge.benefit.dto.CalculationRequest;
import com.example.helmes_challenge.benefit.dto.CalculationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentalBenefitApplicationService {
    private final ParentalBenefitCalculatorService calculatorService;

    public CalculationResult calculate(CalculationRequest request) {
        return calculatorService.calculate(request);
    }
}
