package com.ing.mortgagecalculator.controller;

import com.ing.mortgagecalculator.model.MortgageCheckRequest;
import com.ing.mortgagecalculator.model.MortgageCheckResponse;
import com.ing.mortgagecalculator.model.MortgageRate;
import com.ing.mortgagecalculator.service.MortgageCalculatorService;
import com.ing.mortgagecalculator.service.ProdMortgageCalculatorService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Valid
public class MortgageCalculatorController {

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
        return mortgageCalculatorService.checkMortgage(request);
    }

}
