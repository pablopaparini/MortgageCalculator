package com.ing.mortgagecalculator.model;

import java.sql.Timestamp;

public record MortgageRate(int maturityPeriod, double interestRate, Timestamp lastUpdate) {}
