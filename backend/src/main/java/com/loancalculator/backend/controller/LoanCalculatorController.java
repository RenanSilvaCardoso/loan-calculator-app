package com.loancalculator.backend.controller;

import com.loancalculator.backend.dto.LoanCalculatorRequest;
import com.loancalculator.backend.dto.LoanCalculatorResponse;
import com.loancalculator.backend.service.LoanCalculatorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calculator")
public class LoanCalculatorController {

    private final LoanCalculatorService loanCalculatorService;

    public LoanCalculatorController(LoanCalculatorService loanCalculatorService){
        this.loanCalculatorService = loanCalculatorService;
    }

    @PostMapping("/calculate")
    public LoanCalculatorResponse calculate(@Valid @RequestBody LoanCalculatorRequest request){

        return loanCalculatorService.calculate(request);
    }
}
