import { useState } from 'react'
import './App.scss'
import { LoanForm } from './components/loan-form/loan-form.tsx'
import { LoanTable } from './components/loan-table/loan-table.tsx'
import { calculateLoan } from './services/loanService.ts'
import type { LoanFormValues, LoanScheduleRow } from './types/loan.ts'

function App() {
    const [isLoading, setIsLoading] = useState(false);
    const [installments, setInstallments] = useState<LoanScheduleRow[]>([]);
    const [error, setError] = useState<string | null>(null);

    const handleCalculate = async (values: LoanFormValues) => {
        setIsLoading(true);
        setError(null);
        try {
            const data = await calculateLoan(values);
            setInstallments(data.LoanScheduleRows);
        } catch (err) {
            setInstallments([]);
            setError(err instanceof Error ? err.message : 'Erro inesperado ao calcular o empréstimo.');
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <main>
            <h1>Calculadora de empréstimos</h1>
            <LoanForm onSubmit={handleCalculate} isLoading={isLoading} />
            {error && (
                <p className="app__error" role="alert">
                    {error}
                </p>
            )}
            {installments.length > 0 && <LoanTable installments={installments} />}
        </main>
    )
}

export default App
