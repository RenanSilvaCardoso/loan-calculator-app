import type { LoanCalculatorRequest, LoanCalculatorResponse, LoanFormValues } from '../types/loan.ts'

export async function calculateLoan(values: LoanFormValues): Promise<LoanCalculatorResponse> {
    const request: LoanCalculatorRequest = {
        ...values,
        loanAmount: Number(values.loanAmount),
        interestRatePercent: Number(values.interestRatePercent),
    }

    let response: Response
    try {
        response = await fetch('/api/calculator/calculate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(request),
        })
    } catch {
        throw new Error('Não foi possível conectar ao servidor. Tente novamente.')
    }

    if (!response.ok) {
        const body = await response.json().catch(() => null)
        const message = Array.isArray(body?.errors) ? body.errors.join(' ') : ''
        throw new Error(message || 'Erro ao calcular o empréstimo.')
    }

    return response.json()
}
