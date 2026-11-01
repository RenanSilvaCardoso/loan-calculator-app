import { useState, type FormEvent } from 'react'
import './loan-form.scss'
import type { LoanFormValues } from '../../types/loan.ts'
import { formatCurrency, parseCurrency } from '../../utils/formatters.ts'

interface LoanFormProps {
    onSubmit: (values: LoanFormValues) => Promise<void>
    isLoading: boolean
}

export function LoanForm({ onSubmit, isLoading }: LoanFormProps) {
    const [form, setForm] = useState<LoanFormValues>({
        initialDate: '',
        finalDate: '',
        firstPaymentDate: '',
        loanAmount: '',
        interestRatePercent: '',
    })

    function handleChange(field: keyof LoanFormValues, value: string) {
        setForm({ ...form, [field]: value })
    }

    const allFilled = Object.values(form).every((value) => value !== '');
    const validFinalDate = form.finalDate > form.initialDate;
    const validFirstPayment = form.firstPaymentDate > form.initialDate && form.firstPaymentDate < form.finalDate;
    const validNumbers = Number(form.loanAmount) > 0 && Number(form.interestRatePercent) > 0;

    const isValid = allFilled && validFinalDate && validFirstPayment && validNumbers

    function handleSubmit(event: FormEvent) {
        event.preventDefault()
        onSubmit(form)
    }

    return (
        <form className="loan-form" onSubmit={handleSubmit}>
            <div className="loan-form_field">
                <label htmlFor="initialDate">Data Inicial</label>
                <input
                    id="initialDate"
                    type="date"
                    value={form.initialDate}
                    onChange={(e) => handleChange('initialDate', e.target.value)}
                />
            </div>

            <div className="loan-form_field">
                <label htmlFor="finalDate">Data Final</label>
                <input
                    id="finalDate"
                    type="date"
                    value={form.finalDate}
                    onChange={(e) => handleChange('finalDate', e.target.value)}
                />
            </div>

            <div className="loan-form_field">
                <label htmlFor="firstPaymentDate">Primeiro Pagamento</label>
                <input
                    id="firstPaymentDate"
                    type="date"
                    value={form.firstPaymentDate}
                    onChange={(e) => handleChange('firstPaymentDate', e.target.value)}
                />
            </div>

            <div className="loan-form_field">
                <label htmlFor="loanAmount">Valor de Empréstimo</label>
                <input
                    id="loanAmount"
                    type="text"
                    inputMode="numeric"
                    placeholder="R$ 0,00"
                    value={formatCurrency(form.loanAmount)}
                    onChange={(e) => handleChange('loanAmount', parseCurrency(e.target.value))}
                />
            </div>

            <div className="loan-form_field">
                <label htmlFor="interestRatePercent">Taxa de Juros (%)</label>
                <input
                    id="interestRatePercent"
                    type="number"
                    step="0.01"
                    value={form.interestRatePercent}
                    onChange={(e) => handleChange('interestRatePercent', e.target.value)}
                />
            </div>

            <button type="submit" disabled={!isValid || isLoading}>
                {isLoading ? 'Calculando...' : 'Calcular'}
            </button>

            {allFilled && !validFinalDate && (
                <p className="loan-form_error">A data final deve ser maior que a data inicial.</p>
            )}
            {allFilled && validFinalDate && !validFirstPayment && (
                <p className="loan-form_error">O primeiro pagamento deve estar entre a data inicial e a final.</p>
            )}
        </form>
    )
}
