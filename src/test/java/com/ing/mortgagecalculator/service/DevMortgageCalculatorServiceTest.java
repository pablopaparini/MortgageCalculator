package com.ing.mortgagecalculator.service;

import com.ing.mortgagecalculator.model.MortgageCheckRequest;
import com.ing.mortgagecalculator.model.MortgageCheckResponse;
import com.ing.mortgagecalculator.model.MortgageRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class DevMortgageCalculatorServiceTest {

  @Autowired private DevMortgageCalculatorService service;

  @Test
  void testGetMortgageRates() {
    List<MortgageRate> rates = service.getMortgageRates();
    assertNotNull(rates);
    assertTrue(rates.isEmpty());
  }

  @Test
  void testCheckMortgage() {
    MortgageCheckRequest request =
        new MortgageCheckRequest(
            new BigDecimal("60000"), 30, new BigDecimal("240000"), new BigDecimal("50000"));
    MortgageCheckResponse response = service.checkMortgage(request);
    assertNull(response);
  }
}
