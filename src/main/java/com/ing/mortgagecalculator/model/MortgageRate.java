package com.ing.mortgagecalculator.model;

import java.time.LocalDateTime;

public record MortgageRate(int maturityPeriod, double interestRate, LocalDateTime lastUpdate) {}
