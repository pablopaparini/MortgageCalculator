package com.ing.mortgagecalculator.service;

import com.ing.mortgagecalculator.config.MortgageRateProperties;
import com.ing.mortgagecalculator.model.MortgageCheckRequest;
import com.ing.mortgagecalculator.model.MortgageCheckResponse;
import com.ing.mortgagecalculator.model.MortgageRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProdMortgageCalculatorServiceTest {

  private ProdMortgageCalculatorService service;

  @BeforeEach
  void setUp() {
    MortgageRateProperties properties = new MortgageRateProperties();
    properties.setRates(
        List.of(
            new MortgageRate(30, 3.5, Timestamp.valueOf("2023-10-01 00:00:00")),
            new MortgageRate(15, 2.8, Timestamp.valueOf("2023-10-01 00:00:00"))));
    service = new ProdMortgageCalculatorService(properties);
    ReflectionTestUtils.setField(service, "incomeTimeLimit", 4);
  }

  @Test
  void testGetMortgageRates() {
    List<MortgageRate> rates = service.getMortgageRates();
    assertEquals(2, rates.size());
  }

  @Test
  void testCheckMortgage() {
    MortgageCheckRequest request =
        new MortgageCheckRequest(
            new BigDecimal("60000"), 30, new BigDecimal("240000"), new BigDecimal("300000"));
    MortgageCheckResponse response = service.checkMortgage(request);
    assertTrue(response.feasible());
  }

  @Test
  void testCheckMortgageRateNotFound() {
    MortgageCheckRequest request =
        new MortgageCheckRequest(
            new BigDecimal("60000"),
            25, // 25 years maturity period not in rates
            new BigDecimal("240000"),
            new BigDecimal("300000"));
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              service.checkMortgage(request);
            });
    assertEquals("Interest rate for the given maturity period not found", exception.getMessage());
  }

  @Test
  void testCheckMortgageDenominatorZero() {
    MortgageRateProperties properties = new MortgageRateProperties();
    properties.setRates(
            List.of(
                    new MortgageRate(30, 0.0, Timestamp.valueOf("2023-10-01 00:00:00")),
                    new MortgageRate(15, 2.8, Timestamp.valueOf("2023-10-01 00:00:00"))));
    service = new ProdMortgageCalculatorService(properties);
    ReflectionTestUtils.setField(service, "incomeTimeLimit", 4);
    MortgageCheckRequest request =
        new MortgageCheckRequest(
            new BigDecimal("60000"),
            30,
            new BigDecimal("240000"),
            new BigDecimal("300000"));
    Exception exception =
        assertThrows(
            ArithmeticException.class,
            () -> {
              service.checkMortgage(request);
            });
    assertEquals("Denominator is zero, cannot divide by zero", exception.getMessage());
  }

    @Test
    void testCheckMortgageNotFeasible() {
        MortgageCheckRequest request =
                new MortgageCheckRequest(
                        new BigDecimal("60000"), 30, new BigDecimal("240000"), new BigDecimal("50000"));
        MortgageCheckResponse response = service.checkMortgage(request);
        assertFalse(response.feasible());
    }

    @Test
    void testCheckMortgageNotFeasible2() {
        MortgageCheckRequest request =
                new MortgageCheckRequest(
                        new BigDecimal("50000"), 30, new BigDecimal("240000"), new BigDecimal("1000000"));
        MortgageCheckResponse response = service.checkMortgage(request);
        assertFalse(response.feasible());
    }
}
