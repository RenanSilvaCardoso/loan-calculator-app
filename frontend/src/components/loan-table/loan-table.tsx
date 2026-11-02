import './loan-table.scss'
import type { LoanScheduleRow } from '../../types/loan.ts'
import { formatDate, formatNumber } from '../../utils/formatters.ts'

interface LoanTableProps {
    installments: LoanScheduleRow[]
}

export function LoanTable({ installments }: LoanTableProps) {
    return (
        <div className="loan-table">
            <table>
                <thead>
                <tr>
                    <th scope="colgroup" colSpan={3}>Empréstimo</th>
                    <th scope="colgroup" colSpan={2} className="loan-table__group-start">Parcela</th>
                    <th scope="colgroup" colSpan={2} className="loan-table__group-start">Principal</th>
                    <th scope="colgroup" colSpan={3} className="loan-table__group-start">Juros</th>
                </tr>
                <tr>
                    <th scope="col">Data Competência</th>
                    <th scope="col">Valor de Empréstimo</th>
                    <th scope="col">Saldo Devedor</th>
                    <th scope="col" className="loan-table__group-start">Consolidada</th>
                    <th scope="col">Total</th>
                    <th scope="col" className="loan-table__group-start">Amortização</th>
                    <th scope="col">Saldo</th>
                    <th scope="col" className="loan-table__group-start">Provisão</th>
                    <th scope="col">Acumulado</th>
                    <th scope="col">Pago</th>
                </tr>
                </thead>
                <tbody>
                {installments.map((installment) => (
                    <tr key={installment.competenceDate}>
                        <td className="loan-table__center">{formatDate(installment.competenceDate)}</td>
                        <td>{formatNumber(installment.loanAmount)}</td>
                        <td>{formatNumber(installment.outstandingBalance)}</td>
                        <td className="loan-table__group-start loan-table__center">{installment.consolidated}</td>
                        <td>{formatNumber(installment.installmentTotal)}</td>
                        <td className="loan-table__group-start">{formatNumber(installment.amortization)}</td>
                        <td>{formatNumber(installment.principalBalance)}</td>
                        <td className="loan-table__group-start">{formatNumber(installment.provision)}</td>
                        <td>{formatNumber(installment.accumulatedInterest)}</td>
                        <td>{formatNumber(installment.paidInterest)}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}
