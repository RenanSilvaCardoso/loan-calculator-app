export function formatCurrency(value: string): string {
    if (value === '') return ''

    const [integerPart, decimalPart] = value.split('.')
    const formatted = Number(integerPart).toLocaleString('pt-BR')

    return decimalPart === undefined ? `R$ ${formatted}` : `R$ ${formatted},${decimalPart}`
}

export function parseCurrency(input: string): string {
    const cleaned = input.replace(/[^\d,]/g, '')
    if (cleaned === '') return ''

    const [integerPart, decimalPart] = cleaned.split(',')
    const integer = integerPart.replace(/^0+(?=\d)/, '') || '0'

    return decimalPart === undefined ? integer : `${integer}.${decimalPart.slice(0, 2)}`
}

export function formatNumber(value: number): string {
    return value.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

// Recebe "2024-01-31" e devolve "31/01/2024"
export function formatDate(value: string): string {
    const [year, month, day] = value.split('-')

    return `${day}/${month}/${year}`
}
