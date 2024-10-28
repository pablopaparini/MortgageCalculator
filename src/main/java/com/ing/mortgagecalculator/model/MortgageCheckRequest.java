package com.ing.mortgagecalculator.model;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MortgageCheckRequest(
    @NotNull BigDecimal income,
    @NotNull int maturityPeriod,
    @NotNull BigDecimal loanValue,
    @NotNull BigDecimal homeValue) {}
