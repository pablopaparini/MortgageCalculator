package com.ing.mortgagecalculator.controller;

import com.ing.mortgagecalculator.model.MortgageCheckRequest;
import com.ing.mortgagecalculator.model.MortgageCheckResponse;
import com.ing.mortgagecalculator.model.MortgageRate;
import com.ing.mortgagecalculator.service.MortgageCalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Valid
public class MortgageCalculatorController {

    private static final Logger logger = LoggerFactory.getLogger(MortgageCalculatorController.class);


    private final MortgageCalculatorService mortgageCalculatorService;

    public MortgageCalculatorController(MortgageCalculatorService mortgageCalculatorService) {
        this.mortgageCalculatorService = mortgageCalculatorService;
    }


    @Operation(summary = "Get a list of current interest rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/interest-rates")
    public List<MortgageRate> getMortgageRates(){
        logger.info("Fetching mortgage rates");
        return mortgageCalculatorService.getMortgageRates();
    }

    @Operation(summary = "Post the parameters to calculate for a mortgage check")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated mortgage check"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/mortgage-check")
    public MortgageCheckResponse checkMortgage(@Valid @RequestBody MortgageCheckRequest request) {
        logger.info("Checking mortgage for request: {}", request);
        return mortgageCalculatorService.checkMortgage(request);
    }

}
