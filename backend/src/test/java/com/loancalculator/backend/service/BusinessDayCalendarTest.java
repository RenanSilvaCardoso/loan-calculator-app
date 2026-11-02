package com.loancalculator.backend.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusinessDayCalendarTest {

    private final BusinessDayCalendar calendar = new BusinessDayCalendar();

    @Test
    void keepsBusinessDay() {
        assertEquals(LocalDate.of(2024, 2, 15), calendar.nextBusinessDay(LocalDate.of(2024, 2, 15)));
    }

    @Test
    void movesWeekendToMonday() {
        assertEquals(LocalDate.of(2024, 6, 17), calendar.nextBusinessDay(LocalDate.of(2024, 6, 15))); // sábado
        assertEquals(LocalDate.of(2024, 9, 16), calendar.nextBusinessDay(LocalDate.of(2024, 9, 15))); // domingo
    }

    @Test
    void movesFixedHoliday() {
        assertEquals(LocalDate.of(2024, 11, 18), calendar.nextBusinessDay(LocalDate.of(2024, 11, 15)));
    }

    @Test
    void movesEasterBasedHolidays() {
        assertEquals(LocalDate.of(2026, 2, 18), calendar.nextBusinessDay(LocalDate.of(2026, 2, 15)));
        assertEquals(LocalDate.of(2028, 6, 16), calendar.nextBusinessDay(LocalDate.of(2028, 6, 15)));
        assertEquals(LocalDate.of(2033, 4, 18), calendar.nextBusinessDay(LocalDate.of(2033, 4, 15)));
    }

    @Test
    void blackConsciousnessDayOnlySince2024() {
        assertTrue(calendar.isBusinessDay(LocalDate.of(2023, 11, 20)));
        assertFalse(calendar.isBusinessDay(LocalDate.of(2024, 11, 20)));
    }
}