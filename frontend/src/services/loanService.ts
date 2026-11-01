import type { LoanCalculatorRequest, LoanCalculatorResponse, LoanFormValues } from '../types/loan.ts'

export async function calculateLoan(values: LoanFormValues): Promise<LoanCalculatorResponse> {
    const request: LoanCalculatorRequest = {
        ...values,
        loanAmount: Number(values.loanAmount),
        interestRatePercent: Number(values.interestRatePercent),
    }

    const response = await fetch('/api/calculator/calculate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request),
    })

    if (!response.ok) {
        throw new Error('Erro ao calcular o empréstimo')
    }

    return response.json()
}
