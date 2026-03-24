package com.example.helmes_challenge.benefit;

import com.example.helmes_challenge.benefit.dto.CalculationRequest;
import com.example.helmes_challenge.benefit.dto.CalculationResult;
import com.example.helmes_challenge.benefit.dto.MonthlyBenefitBreakdown;
import com.example.helmes_challenge.benefit.dto.ParentalBenefitApplicationResponse;
import com.example.helmes_challenge.benefit.service.ParentalBenefitApplicationService;
import com.example.helmes_challenge.common.exception.ApiException;
import com.example.helmes_challenge.common.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ParentalBenefitControllerTest {

    @Mock
    private ParentalBenefitApplicationService applicationService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ParentalBenefitController controller = new ParentalBenefitController(applicationService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void calculate_returnsOkWithApiEnvelope() throws Exception {
        when(applicationService.calculate(any(CalculationRequest.class))).thenReturn(sampleCalculationResult());

        String payload = """
                {
                  "grossSalary": 3000.00,
                  "babyBirthDate": "2026-03-10"
                }
                """;

        mockMvc.perform(post("/api/v1/parental-benefits/calculations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Calculation successful"))
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.monthlyEligibleSalary").value(3000.00))
                .andExpect(jsonPath("$.data.dailyRate").value(100.00))
                .andExpect(jsonPath("$.data.totalPayment").value(35600.00))
                .andExpect(jsonPath("$.data.breakdown.length()").value(1));
    }

    @Test
    void create_returnsCreatedWithSavedId() throws Exception {
        UUID id = UUID.randomUUID();

        when(applicationService.create(any(CalculationRequest.class))).thenReturn(sampleApplicationResponse(id));

        String payload = """
                {
                  "grossSalary": 3000.00,
                  "babyBirthDate": "2026-03-10"
                }
                """;

        mockMvc.perform(post("/api/v1/parental-benefits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Parental benefit created successfully"))
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.grossSalary").value(3000.00))
                .andExpect(jsonPath("$.data.babyBirthDate").value("2026-03-10"));
    }

    @Test
    void getById_returnsNotFoundWhenMissing() throws Exception {
        UUID missingId = UUID.randomUUID();
        when(applicationService.getById(eq(missingId)))
                .thenThrow(new ApiException("Parental benefit not found", HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/v1/parental-benefits/{id}", missingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Parental benefit not found"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    void calculate_returnsBadRequestForInvalidDateFormat() throws Exception {
        String invalidPayload = """
                {
                  "grossSalary": 3000.00,
                  "babyBirthDate": "03-10-2026"
                }
                """;

        mockMvc.perform(post("/api/v1/parental-benefits/calculations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid date format. Please use a valid date"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    private CalculationResult sampleCalculationResult() {
        return new CalculationResult(
                new BigDecimal("3000.00"),
                new BigDecimal("100.00"),
                new BigDecimal("35600.00"),
                List.of(new MonthlyBenefitBreakdown(2026, 3, 22, new BigDecimal("2200.00")))
        );
    }

    private ParentalBenefitApplicationResponse sampleApplicationResponse(UUID id) {
        return new ParentalBenefitApplicationResponse(
                id,
                new BigDecimal("3000.00"),
                LocalDate.of(2026, 3, 10),
                sampleCalculationResult()
        );
    }
}
