package com.loancalculator.backend.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanCalculatorRequest(
        @NotNull LocalDate initialDate,
        @NotNull LocalDate finalDate,
        @NotNull LocalDate firstPaymentDate,
        @NotNull @Positive BigDecimal loanAmount,
        @NotNull @Positive BigDecimal interestRatePercent
) {
    @AssertTrue(message = "The end date must be after than the start date")
    public boolean isFinalDateAfterInitialDate() {
        if (initialDate == null || finalDate == null) return true;

        return finalDate.isAfter(initialDate);
    }

    @AssertTrue(message = "The first payment date must be between initial date and final date")
    public boolean isFirstPaymentDateWithinRange() {
        if (initialDate == null || finalDate == null || firstPaymentDate == null) return true;

        return firstPaymentDate.isAfter(initialDate) && firstPaymentDate.isBefore(finalDate);
    }
}
