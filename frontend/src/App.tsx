import { useState } from 'react'
import './App.scss'
import { LoanForm } from './components/loan-form/loan-form.tsx'
import { LoanTable } from './components/loan-table/loan-table.tsx'
import { calculateLoan } from './services/loanService.ts'
import type { LoanFormValues, LoanInstallment } from './types/loan.ts'

function App() {
    const [isLoading, setIsLoading] = useState(false)
    const [installments, setInstallments] = useState<LoanInstallment[]>([])

    const handleCalculate = async (values: LoanFormValues) => {
        setIsLoading(true)
        try {
            const data = await calculateLoan(values)
            setInstallments(data.LoanInstallments)
        } finally {
            setIsLoading(false)
        }
    }

    return (
        <main>
            <h1>Calculadora de empréstimos</h1>
            <LoanForm onSubmit={handleCalculate} isLoading={isLoading} />
            {installments.length > 0 && <LoanTable installments={installments} />}
        </main>
    )
}

export default App
