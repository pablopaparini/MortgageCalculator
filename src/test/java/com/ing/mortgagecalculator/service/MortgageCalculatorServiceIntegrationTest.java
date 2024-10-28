package com.ing.mortgagecalculator.service;

import com.ing.mortgagecalculator.model.MortgageCheckRequest;
import com.ing.mortgagecalculator.model.MortgageCheckResponse;
import com.ing.mortgagecalculator.model.MortgageRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MortgageCalculatorServiceIntegrationTest {

    @Autowired
    private ProdMortgageCalculatorService mortgageCalculatorService;

    @Test
    void testGetMortgageRates() {
        List<MortgageRate> rates = mortgageCalculatorService.getMortgageRates();
        assertNotNull(rates);
        assertFalse(rates.isEmpty());
    }

    @Test
    void testCheckMortgage() {
        MortgageCheckRequest request = new MortgageCheckRequest(
                BigDecimal.valueOf(60000),
                30,
                BigDecimal.valueOf(240000),
                BigDecimal.valueOf(300000)
        );

        MortgageCheckResponse response = mortgageCalculatorService.checkMortgage(request);
        assertNotNull(response);
        assertTrue(response.feasible());
        assertNotNull(response.monthlyCosts());
        assertEquals(BigDecimal.valueOf(1077.7072560559), response.monthlyCosts());
    }
}