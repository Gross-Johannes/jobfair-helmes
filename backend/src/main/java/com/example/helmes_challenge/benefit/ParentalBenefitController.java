package com.example.helmes_challenge.benefit;

import com.example.helmes_challenge.benefit.dto.CalculationRequest;
import com.example.helmes_challenge.benefit.dto.CalculationResult;
import com.example.helmes_challenge.benefit.dto.ParentalBenefitApplicationResponse;
import com.example.helmes_challenge.benefit.service.ParentalBenefitApplicationService;
import com.example.helmes_challenge.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/parental-benefits")
@RequiredArgsConstructor
public class ParentalBenefitController {
    private final ParentalBenefitApplicationService applicationService;

    @PostMapping("/calculations")
    public ResponseEntity<ApiResponse<CalculationResult>> calculate(
            @RequestBody @Valid CalculationRequest request) {

        CalculationResult result = applicationService.calculate(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.<CalculationResult>builder()
                        .message("Calculation successful")
                        .data(result)
                        .errors(null)
                        .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ParentalBenefitApplicationResponse>> create(
            @RequestBody @Valid CalculationRequest request) {

        ParentalBenefitApplicationResponse result = applicationService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<ParentalBenefitApplicationResponse>builder()
                        .message("Parental benefit created successfully")
                        .data(result)
                        .errors(null)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParentalBenefitApplicationResponse>> getById(@PathVariable UUID id) {

        ParentalBenefitApplicationResponse result = applicationService.getById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.<ParentalBenefitApplicationResponse>builder()
                        .message("Parental benefit retrieved successfully")
                        .data(result)
                        .errors(null)
                        .build());
    }
}
