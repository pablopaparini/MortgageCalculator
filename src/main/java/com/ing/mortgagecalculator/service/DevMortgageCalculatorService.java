package com.ing.mortgagecalculator.service;

import com.ing.mortgagecalculator.model.MortgageCheckRequest;
import com.ing.mortgagecalculator.model.MortgageCheckResponse;
import com.ing.mortgagecalculator.model.MortgageRate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
public class DevMortgageCalculatorService implements MortgageCalculatorService{
    @Override
    public List<MortgageRate> getMortgageRates() {
        return List.of();
    }

    @Override
    public MortgageCheckResponse checkMortgage(MortgageCheckRequest request) {
        return null;
    }
}
