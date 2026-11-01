import { useState } from 'react'
import './App.scss'
import { LoanForm } from './components/loan-form/loan-form.tsx'
import { calculateLoan } from './services/loanService.ts'
import type { LoanFormValues } from './types/loan.ts'

function App() {
    const [isLoading, setIsLoading] = useState(false)

    const handleCalculate = async (values: LoanFormValues) => {
        setIsLoading(true)
        try {
            const data = await calculateLoan(values)
            console.log(data) // depois vai para a grid
        } finally {
            setIsLoading(false)
        }
    }

    return (
        <main>
            <h1>Calculadora de empréstimos</h1>
            <LoanForm onSubmit={handleCalculate} isLoading={isLoading} />
        </main>
    )
}

export default App
