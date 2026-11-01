import './loan-table.scss'
import type { LoanInstallment } from '../../types/loan.ts'
import { formatDate, formatNumber } from '../../utils/formatters.ts'

interface LoanTableProps {
    installments: LoanInstallment[]
}

export function LoanTable({ installments }: LoanTableProps) {
    return (
        <div className="loan-table">
            <table>
                <thead>
                    <tr>
                        <th colSpan={3}>Empréstimo</th>
                        <th colSpan={2} className="loan-table_group-start">Parcela</th>
                        <th colSpan={2} className="loan-table_group-start">Principal</th>
                        <th colSpan={3} className="loan-table_group-start">Juros</th>
                    </tr>
                    <tr>
                        <th>Data Competência</th>
                        <th>Valor de Empréstimo</th>
                        <th>Saldo Devedor</th>
                        <th className="loan-table_group-start">Consolidada</th>
                        <th>Total</th>
                        <th className="loan-table_group-start">Amortização</th>
                        <th>Saldo</th>
                        <th className="loan-table_group-start">Provisão</th>
                        <th>Acumulado</th>
                        <th>Pago</th>
                    </tr>
                </thead>
                <tbody>
                    {installments.map((installment) => (
                        <tr key={installment.competenceDate}>
                            <td className="loan-table_center">{formatDate(installment.competenceDate)}</td>
                            <td>{formatNumber(installment.loanAmount)}</td>
                            <td>{formatNumber(installment.outstandingBalance)}</td>
                            <td className="loan-table_group-start loan-table_center">{installment.consolidated}</td>
                            <td>{formatNumber(installment.installmentTotal)}</td>
                            <td className="loan-table_group-start">{formatNumber(installment.amortization)}</td>
                            <td>{formatNumber(installment.principalBalance)}</td>
                            <td className="loan-table_group-start">{formatNumber(installment.provision)}</td>
                            <td>{formatNumber(installment.accumulatedInterest)}</td>
                            <td>{formatNumber(installment.paidInterest)}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )
}
