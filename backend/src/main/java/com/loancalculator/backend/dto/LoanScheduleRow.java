package com.loancalculator.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanScheduleRow(
        LocalDate competenceDate,
        BigDecimal loanAmount,
        BigDecimal outstandingBalance,
        String consolidated,
        BigDecimal installmentTotal,
        BigDecimal amortization,
        BigDecimal principalBalance,
        BigDecimal provision,
        BigDecimal accumulatedInterest,
        BigDecimal paidInterest
) {
}