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
