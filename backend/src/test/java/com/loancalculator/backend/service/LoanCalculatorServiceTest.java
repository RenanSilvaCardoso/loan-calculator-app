package com.loancalculator.backend.service;

import com.loancalculator.backend.dto.LoanCalculatorRequest;
import com.loancalculator.backend.dto.LoanScheduleRow;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Valores esperados extraídos da planilha Calculadora_Emprestimos.xlsx
 * (01/01/2024 a 01/01/2034, primeiro pagamento 15/02/2024, R$ 140.000,00, 7% a.a.).
 */
class LoanCalculatorServiceTest {

    private final LoanCalculatorService service = new LoanCalculatorService(new BusinessDayCalendar());

    private List<LoanScheduleRow> spreadsheetScenario() {
        LoanCalculatorRequest request = new LoanCalculatorRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2034, 1, 1),
                LocalDate.of(2024, 2, 15),
                new BigDecimal("140000"),
                new BigDecimal("7")
        );
        return service.calculate(request).LoanScheduleRows();
    }

    private LoanScheduleRow rowAt(List<LoanScheduleRow> rows, LocalDate date) {
        return rows.stream()
                .filter(row -> row.competenceDate().equals(date))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Linha não encontrada: " + date));
    }

    @Test
    void generatesSameNumberOfRowsAndInstallmentsAsSpreadsheet() {
        List<LoanScheduleRow> rows = spreadsheetScenario();

        assertEquals(241, rows.size());
        assertEquals(120, rows.stream().filter(row -> !row.consolidated().isEmpty()).count());
    }

    @Test
    void firstMonthProvision() {
        LoanScheduleRow row = rowAt(spreadsheetScenario(), LocalDate.of(2024, 1, 31));

        assertEquals(new BigDecimal("791.58"), row.provision());
    }

    @Test
    void paymentOnSaturdayMovesToMonday() {
        LoanScheduleRow row = rowAt(spreadsheetScenario(), LocalDate.of(2024, 6, 17));

        assertEquals("5/120", row.consolidated());
        assertEquals(new BigDecimal("434.38"), row.provision());
        assertEquals(new BigDecimal("841.95"), row.paidInterest());
        assertEquals(new BigDecimal("2008.62"), row.installmentTotal());
    }

    @Test
    void paymentAfterCarnivalMovesToWednesday() {
        LoanScheduleRow row = rowAt(spreadsheetScenario(), LocalDate.of(2026, 2, 18));

        assertEquals("25/120", row.consolidated());
        assertEquals(new BigDecimal("717.97"), row.paidInterest());
        assertEquals(new BigDecimal("1884.64"), row.installmentTotal());
    }

    @Test
    void lastRowSettlesTheLoan() {
        List<LoanScheduleRow> rows = spreadsheetScenario();
        LoanScheduleRow last = rows.get(rows.size() - 1);

        assertEquals(LocalDate.of(2034, 1, 1), last.competenceDate());
        assertEquals("120/120", last.consolidated());
        assertEquals(new BigDecimal("1170.40"), last.installmentTotal());
        assertEquals(0, last.principalBalance().signum());
        assertEquals(0, last.accumulatedInterest().signum());
    }

    @Test
    void endOfMonthPaymentKeepsEndOfMonth() {
        LoanCalculatorRequest request = new LoanCalculatorRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 4, 30),
                LocalDate.of(2024, 1, 31),
                new BigDecimal("1000"),
                new BigDecimal("7")
        );
        List<LoanScheduleRow> rows = service.calculate(request).LoanScheduleRows();

        assertEquals("1/4", rowAt(rows, LocalDate.of(2024, 1, 31)).consolidated());
        assertEquals("2/4", rowAt(rows, LocalDate.of(2024, 2, 29)).consolidated());
        assertEquals("3/4", rowAt(rows, LocalDate.of(2024, 4, 1)).consolidated()); // 31/03/2024 é domingo
        assertEquals("4/4", rowAt(rows, LocalDate.of(2024, 4, 30)).consolidated());
    }
}