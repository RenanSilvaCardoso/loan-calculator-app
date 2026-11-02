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

    const showFinalDateError = allFilled && !validFinalDate
    const showFirstPaymentError = allFilled && validFinalDate && !validFirstPayment

    function handleSubmit(event: FormEvent) {
        event.preventDefault()
        onSubmit(form)
    }

    return (
        <form className="loan-form" onSubmit={handleSubmit}>
            <div className="loan-form__field">
                <label htmlFor="initialDate">Data Inicial</label>
                <input
                    id="initialDate"
                    type="date"
                    required
                    value={form.initialDate}
                    onChange={(e) => handleChange('initialDate', e.target.value)}
                />
            </div>

            <div className="loan-form__field">
                <label htmlFor="finalDate">Data Final</label>
                <input
                    id="finalDate"
                    type="date"
                    required
                    aria-invalid={showFinalDateError}
                    aria-describedby={showFinalDateError ? 'finalDateError' : undefined}
                    value={form.finalDate}
                    onChange={(e) => handleChange('finalDate', e.target.value)}
                />
            </div>

            <div className="loan-form__field">
                <label htmlFor="firstPaymentDate">Primeiro Pagamento</label>
                <input
                    id="firstPaymentDate"
                    type="date"
                    required
                    aria-invalid={showFirstPaymentError}
                    aria-describedby={showFirstPaymentError ? 'firstPaymentDateError' : undefined}
                    value={form.firstPaymentDate}
                    onChange={(e) => handleChange('firstPaymentDate', e.target.value)}
                />
            </div>

            <div className="loan-form__field">
                <label htmlFor="loanAmount">Valor de Empréstimo</label>
                <input
                    id="loanAmount"
                    type="text"
                    inputMode="numeric"
                    required
                    placeholder="R$ 0,00"
                    value={formatCurrency(form.loanAmount)}
                    onChange={(e) => handleChange('loanAmount', parseCurrency(e.target.value))}
                />
            </div>

            <div className="loan-form__field">
                <label htmlFor="interestRatePercent">Taxa de Juros (%)</label>
                <input
                    id="interestRatePercent"
                    type="number"
                    step="0.01"
                    required
                    value={form.interestRatePercent}
                    onChange={(e) => handleChange('interestRatePercent', e.target.value)}
                />
            </div>

            <button type="submit" disabled={!isValid || isLoading}>
                {isLoading ? 'Calculando...' : 'Calcular'}
            </button>

            {showFinalDateError && (
                <p id="finalDateError" className="loan-form__error" role="alert">
                    A data final deve ser maior que a data inicial.
                </p>
            )}
            {showFirstPaymentError && (
                <p id="firstPaymentDateError" className="loan-form__error" role="alert">
                    O primeiro pagamento deve estar entre a data inicial e a final.
                </p>
            )}
        </form>
    )
}
