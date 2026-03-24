package com.example.helmes_challenge.benefit.service;

import com.example.helmes_challenge.benefit.ParentalBenefit;
import com.example.helmes_challenge.benefit.ParentalBenefitRepository;
import com.example.helmes_challenge.benefit.dto.CalculationRequest;
import com.example.helmes_challenge.benefit.dto.CalculationResult;
import com.example.helmes_challenge.benefit.dto.MonthlyBenefitBreakdown;
import com.example.helmes_challenge.benefit.dto.ParentalBenefitApplicationResponse;
import com.example.helmes_challenge.common.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParentalBenefitApplicationServiceTest {

    @Mock
    private ParentalBenefitCalculatorService calculatorService;

    @Mock
    private ParentalBenefitRepository parentalBenefitRepository;

    private ParentalBenefitApplicationService applicationService;

    @BeforeEach
    void setUp() {
        applicationService = new ParentalBenefitApplicationService(calculatorService, parentalBenefitRepository);
    }

    @Test
    void calculate_delegatesToCalculatorService() {
        CalculationRequest request = new CalculationRequest(new BigDecimal("3000.00"), LocalDate.of(2026, 3, 10));
        CalculationResult expected = sampleCalculationResult();

        when(calculatorService.calculate(request)).thenReturn(expected);

        CalculationResult result = applicationService.calculate(request);

        assertThat(result).isEqualTo(expected);
        verify(calculatorService, times(1)).calculate(request);
    }

    @Test
    void create_savesInputAndReturnsMappedResponseWithCalculation() {
        CalculationRequest request = new CalculationRequest(new BigDecimal("2800.00"), LocalDate.of(2026, 7, 1));
        CalculationResult calculationResult = sampleCalculationResult();
        UUID savedId = UUID.randomUUID();

        when(parentalBenefitRepository.save(any(ParentalBenefit.class))).thenReturn(ParentalBenefit.builder()
                .id(savedId)
                .grossSalary(request.grossSalary())
                .babyBirthDate(request.babyBirthDate())
                .build());
        when(calculatorService.calculate(any(CalculationRequest.class))).thenReturn(calculationResult);

        ParentalBenefitApplicationResponse result = applicationService.create(request);

        ArgumentCaptor<ParentalBenefit> benefitCaptor = ArgumentCaptor.forClass(ParentalBenefit.class);
        verify(parentalBenefitRepository, times(1)).save(benefitCaptor.capture());

        ParentalBenefit savedRequestEntity = benefitCaptor.getValue();
        assertThat(savedRequestEntity.getGrossSalary()).isEqualByComparingTo("2800.00");
        assertThat(savedRequestEntity.getBabyBirthDate()).isEqualTo(LocalDate.of(2026, 7, 1));

        assertThat(result.id()).isEqualTo(savedId);
        assertThat(result.grossSalary()).isEqualByComparingTo("2800.00");
        assertThat(result.babyBirthDate()).isEqualTo(LocalDate.of(2026, 7, 1));
        assertThat(result.calculationResult()).isEqualTo(calculationResult);
    }

    @Test
    void getById_returnsMappedResponse_whenEntityExists() {
        UUID id = UUID.randomUUID();
        ParentalBenefit stored = ParentalBenefit.builder()
                .id(id)
                .grossSalary(new BigDecimal("3200.00"))
                .babyBirthDate(LocalDate.of(2026, 10, 12))
                .build();
        CalculationResult calculationResult = sampleCalculationResult();

        when(parentalBenefitRepository.findById(id)).thenReturn(Optional.of(stored));
        when(calculatorService.calculate(any(CalculationRequest.class))).thenReturn(calculationResult);

        ParentalBenefitApplicationResponse result = applicationService.getById(id);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.grossSalary()).isEqualByComparingTo("3200.00");
        assertThat(result.babyBirthDate()).isEqualTo(LocalDate.of(2026, 10, 12));
        assertThat(result.calculationResult()).isEqualTo(calculationResult);
    }

    @Test
    void getById_throwsNotFoundException_whenEntityDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(parentalBenefitRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.getById(id))
                .isInstanceOf(ApiException.class)
                .hasMessage("Parental benefit not found")
                .extracting("status")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    private CalculationResult sampleCalculationResult() {
        return new CalculationResult(
                new BigDecimal("3000.00"),
                new BigDecimal("100.00"),
                new BigDecimal("35600.00"),
                List.of(new MonthlyBenefitBreakdown(2026, 3, 22, new BigDecimal("2200.00")))
        );
    }
}
