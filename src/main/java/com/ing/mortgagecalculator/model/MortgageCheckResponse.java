package com.ing.mortgagecalculator.model;

import java.math.BigDecimal;

public record MortgageCheckResponse(boolean feasible, BigDecimal monthlyCosts) {}
