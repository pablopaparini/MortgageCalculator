package com.ing.mortgagecalculator.service;

import com.ing.mortgagecalculator.config.MortgageRateProperties;
import com.ing.mortgagecalculator.model.MortgageCheckRequest;
import com.ing.mortgagecalculator.model.MortgageCheckResponse;
import com.ing.mortgagecalculator.model.MortgageRate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@Profile("prod")
public class ProdMortgageCalculatorService implements MortgageCalculatorService {

  private static final Long ONE_HUNDRED = 100L;
  private static final Integer TWELVE = 12;
  private static final int SCALE = 10;
  private static final int ZERO = 0;

  @Value("${calculator.income.time.limit}")
  private int incomeTimeLimit;

  private final MortgageRateProperties mortgageRateProperties;

  public ProdMortgageCalculatorService(MortgageRateProperties mortgageRateProperties) {
    this.mortgageRateProperties = mortgageRateProperties;
  }

  public List<MortgageRate> getMortgageRates() {
    return mortgageRateProperties.getRates().stream()
        .map(
            rate -> new MortgageRate(rate.maturityPeriod(), rate.interestRate(), rate.lastUpdate()))
        .toList();
  }

  public MortgageCheckResponse checkMortgage(MortgageCheckRequest request) {
    Optional<MortgageRate> rateOpt =
        mortgageRateProperties.getRates().stream()
            .filter(rate -> rate.maturityPeriod() == request.maturityPeriod())
            .findFirst();

    if (rateOpt.isEmpty()) {
      throw new IllegalArgumentException("Interest rate for the given maturity period not found");
    }

    BigDecimal monthlyInterestRate =
        BigDecimal.valueOf(rateOpt.get().interestRate())
            .divide(BigDecimal.valueOf(ONE_HUNDRED * TWELVE), SCALE, RoundingMode.HALF_UP);
    BigDecimal numerator = monthlyInterestRate.multiply(request.loanValue());
    BigDecimal denominator =
        BigDecimal.ONE.subtract(
            BigDecimal.ONE.divide(
                BigDecimal.ONE.add(monthlyInterestRate).pow(request.maturityPeriod() * TWELVE),
                SCALE,
                RoundingMode.HALF_UP));

    if (denominator.compareTo(BigDecimal.ZERO) == ZERO) {
      throw new ArithmeticException("Denominator is zero, cannot divide by zero");
    }

    BigDecimal monthlyCosts = numerator.divide(denominator, SCALE, RoundingMode.HALF_UP);

    boolean feasible =
        request
                    .loanValue()
                    .compareTo(request.income().multiply(BigDecimal.valueOf(incomeTimeLimit)))
                <= ZERO
            && request.loanValue().compareTo(request.homeValue()) <= ZERO;

    return new MortgageCheckResponse(feasible, monthlyCosts);
  }
}