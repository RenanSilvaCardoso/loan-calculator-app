package com.loancalculator.backend.dto;

import java.util.List;

public record LoanCalculatorResponse(
        List<LoanInstallment> LoanInstallments
){
}
