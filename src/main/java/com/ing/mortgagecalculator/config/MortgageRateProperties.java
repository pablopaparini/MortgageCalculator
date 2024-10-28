package com.ing.mortgagecalculator.config;

import java.sql.Timestamp;
import java.util.List;

import com.ing.mortgagecalculator.model.MortgageRate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mortgage")
public class MortgageRateProperties {

    private List<MortgageRate> rates;

    public List<MortgageRate> getRates() {
        return rates;
    }

    public void setRates(List<MortgageRate> rates) {
        this.rates = rates;
    }
}