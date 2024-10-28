package com.ing.mortgagecalculator.controller;

import com.ing.mortgagecalculator.model.MortgageCheckRequest;
import com.ing.mortgagecalculator.model.MortgageCheckResponse;
import com.ing.mortgagecalculator.model.MortgageRate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MortgageCalculatorControllerIntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void testGetMortgageRates() {
    ResponseEntity<MortgageRate[]> response =
        restTemplate.getForEntity("/api/interest-rates", MortgageRate[].class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().length > 0);
  }

  @Test
  void testCheckMortgage() {
    MortgageCheckRequest request =
        new MortgageCheckRequest(
            BigDecimal.valueOf(60000), 30, BigDecimal.valueOf(240000), BigDecimal.valueOf(300000));

    ResponseEntity<MortgageCheckResponse> response =
        restTemplate.postForEntity("/api/mortgage-check", request, MortgageCheckResponse.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().feasible());
    assertNotNull(response.getBody().monthlyCosts());
  }

  @Test
  void testHandleValidationExceptions() {
    MortgageCheckRequest request =
        new MortgageCheckRequest(null, 30, BigDecimal.valueOf(240000), BigDecimal.valueOf(300000));

    ResponseEntity<Map> response =
        restTemplate.postForEntity("/api/mortgage-check", request, Map.class);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().containsKey("income"));
  }

  @Test
  void testHandleIllegalArgumentException() {
    MortgageCheckRequest request =
        new MortgageCheckRequest(
            BigDecimal.valueOf(60000), 25, BigDecimal.valueOf(240000), BigDecimal.valueOf(300000));

    ResponseEntity<String> response =
        restTemplate.postForEntity("/api/mortgage-check", request, String.class);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Interest rate for the given maturity period not found", response.getBody());
  }

  @Nested
  @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
  @TestPropertySource(
      properties = {
        "mortgage.rates[0].maturityPeriod=30",
        "mortgage.rates[0].interestRate=0.0", // This will cause an ArithmeticException
        "mortgage.rates[0].lastUpdate=2023-10-01 00:00:00",
        "mortgage.rates[1].maturityPeriod=15",
        "mortgage.rates[1].interestRate=2.8",
        "mortgage.rates[1].lastUpdate=2023-10-01 00:00:00",
        "calculator.income.time.limit=4"
      })
  class ArithmeticExceptionTest {

    @Autowired private TestRestTemplate restTemplate;

    @Test
    void testHandleArithmeticException() {
      MortgageCheckRequest request =
          new MortgageCheckRequest(
              BigDecimal.valueOf(60000),
              30,
              BigDecimal.valueOf(240000),
              BigDecimal.valueOf(300000));

      ResponseEntity<String> response =
          restTemplate.postForEntity("/api/mortgage-check", request, String.class);
      assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
      assertEquals("Denominator is zero, cannot divide by zero", response.getBody());
    }
  }
}
