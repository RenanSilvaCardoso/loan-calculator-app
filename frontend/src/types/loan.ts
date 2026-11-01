export interface LoanFormValues {
    initialDate: string
    finalDate: string
    firstPaymentDate: string
    loanAmount: string
    interestRatePercent: string
}

export interface LoanCalculatorRequest {
    initialDate: string
    finalDate: string
    firstPaymentDate: string
    loanAmount: number
    interestRatePercent: number
}

export interface LoanInstallment {
    competenceDate: string
    loanAmount: number
    outstandingBalance: number
    consolidated: string
    installmentTotal: number
    amortization: number
    principalBalance: number
    provision: number
    accumulatedInterest: number
    paidInterest: number
}

export interface LoanCalculatorResponse {
    loanInstallments: LoanInstallment[]
}
