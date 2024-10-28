package com.ing.mortgagecalculator.service;

import com.ing.mortgagecalculator.model.MortgageCheckRequest;
import com.ing.mortgagecalculator.model.MortgageCheckResponse;
import com.ing.mortgagecalculator.model.MortgageRate;

import java.util.List;

public interface MortgageCalculatorService {
    List<MortgageRate> getMortgageRates();
    MortgageCheckResponse checkMortgage(MortgageCheckRequest request);
}
