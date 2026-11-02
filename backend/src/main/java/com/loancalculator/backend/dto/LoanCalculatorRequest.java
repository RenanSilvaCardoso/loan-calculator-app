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
    @AssertTrue(message = "A data final deve ser maior que a data inicial.")
    public boolean isFinalDateAfterInitialDate() {
        if (initialDate == null || finalDate == null) return true;

        return finalDate.isAfter(initialDate);
    }

    @AssertTrue(message = "O primeiro pagamento deve estar entre a data inicial e a final.")
    public boolean isFirstPaymentDateWithinRange() {
        if (initialDate == null || finalDate == null || firstPaymentDate == null) return true;

        return firstPaymentDate.isAfter(initialDate) && firstPaymentDate.isBefore(finalDate);
    }
}
