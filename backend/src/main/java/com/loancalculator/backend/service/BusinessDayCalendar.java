package com.loancalculator.backend.service;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class BusinessDayCalendar {

    private static final List<MonthDay> FIXED_HOLIDAYS = List.of(
            MonthDay.of(1, 1),
            MonthDay.of(4, 21),
            MonthDay.of(5, 1),
            MonthDay.of(9, 7),
            MonthDay.of(10, 12),
            MonthDay.of(11, 2),
            MonthDay.of(11, 15),
            MonthDay.of(12, 25)
    );

    private static final MonthDay BLACK_CONSCIOUSNESS_DAY = MonthDay.of(11, 20);
    private static final int BLACK_CONSCIOUSNESS_DAY_SINCE = 2024;

    public LocalDate nextBusinessDay(LocalDate date) {
        LocalDate nextDate = date;
        while (!isBusinessDay(nextDate)) {
            nextDate = nextDate.plusDays(1);
        }
        return nextDate;
    }

    public boolean isBusinessDay(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            return false;
        }
        return !holidaysOf(date.getYear()).contains(date);
    }

    private Set<LocalDate> holidaysOf(int year) {
        Set<LocalDate> holidays = new HashSet<>();
        for (MonthDay holiday : FIXED_HOLIDAYS) {
            holidays.add(holiday.atYear(year));
        }
        if (year >= BLACK_CONSCIOUSNESS_DAY_SINCE) {
            holidays.add(BLACK_CONSCIOUSNESS_DAY.atYear(year));
        }

        LocalDate easter = easterSunday(year);
        holidays.add(easter.minusDays(48)); // Carnaval (segunda)
        holidays.add(easter.minusDays(47)); // Carnaval (terça)
        holidays.add(easter.minusDays(2)); // Sexta-feira santa
        holidays.add(easter.plusDays(60)); // Corpus Christ
        return holidays;
    }

    private LocalDate easterSunday(int year) {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int month = (h + l - 7 * m + 114) / 31;
        int day = ((h + l - 7 * m + 114) % 31) + 1;
        return LocalDate.of(year, month, day);
    }
}