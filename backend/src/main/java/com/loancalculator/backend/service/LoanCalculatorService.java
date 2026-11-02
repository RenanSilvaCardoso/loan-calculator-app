package com.loancalculator.backend.service;

import com.loancalculator.backend.dto.LoanCalculatorRequest;
import com.loancalculator.backend.dto.LoanCalculatorResponse;
import com.loancalculator.backend.dto.LoanScheduleRow;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class LoanCalculatorService {

    private static final int BASE_DAYS = 360;
    private static final MathContext MC = MathContext.DECIMAL64;

    private final BusinessDayCalendar businessDayCalendar;

    public LoanCalculatorService(BusinessDayCalendar businessDayCalendar) {
        this.businessDayCalendar = businessDayCalendar;
    }

    public LoanCalculatorResponse calculate(LoanCalculatorRequest request){
        List<LocalDate> paymentDates = buildPaymentDates(request);
        List<LocalDate> scheduleDates = buildScheduleDates(request, paymentDates);
        List<LoanScheduleRow> installmentRows = buildRows(request, scheduleDates, paymentDates);

        return new LoanCalculatorResponse(installmentRows);
    }

    private List<LocalDate> buildPaymentDates(LoanCalculatorRequest request){
        List<LocalDate> paymentDates = new ArrayList<>();
        LocalDate firstPayment = request.firstPaymentDate();
        LocalDate finalDate = request.finalDate();

        LocalDate baseDate = firstPayment;
        for (int i = 1; baseDate.isBefore(finalDate); i++) {
            LocalDate paymentDate = businessDayCalendar.nextBusinessDay(baseDate);
            if (paymentDate.isBefore(finalDate)) {
                paymentDates.add(paymentDate);
            }
            baseDate = firstPayment.plusMonths(i);
        }

        paymentDates.add(finalDate);
        return paymentDates;
    }

    private List<LocalDate> buildScheduleDates(LoanCalculatorRequest request, List<LocalDate> paymentDates) {
        LocalDate initialDate = request.initialDate();
        LocalDate finalDate = request.finalDate();

        Set<LocalDate> dates = new TreeSet<>();
        dates.add(initialDate);
        dates.add(finalDate);
        dates.addAll(paymentDates);

        LocalDate monthEnd = YearMonth.from(initialDate).atEndOfMonth();
        while (monthEnd.isBefore(finalDate)) {
            if (monthEnd.isAfter(initialDate)) {
                dates.add(monthEnd);
            }
            monthEnd = YearMonth.from(monthEnd).plusMonths(1).atEndOfMonth();
        }

        return new ArrayList<>(dates);
    }

    private List<LoanScheduleRow> buildRows(LoanCalculatorRequest request,
                                            List<LocalDate> scheduleDates,
                                            List<LocalDate> paymentDates) {
        List<LoanScheduleRow> installmentRows = new ArrayList<>();

        BigDecimal loanAmount = request.loanAmount();
        double annualRate = request.interestRatePercent().doubleValue() / 100;
        int installmentQuantity = paymentDates.size();
        BigDecimal fixedAmortization = loanAmount.divide(BigDecimal.valueOf(installmentQuantity), MC);
        Set<LocalDate> paymentSet = new HashSet<>(paymentDates);

        BigDecimal principalBalance = loanAmount;
        BigDecimal accumulatedInterest = BigDecimal.ZERO;
        LocalDate previousDate = scheduleDates.get(0);
        int installmentNumber = 0;

        installmentRows.add(toRow(previousDate, loanAmount, "", BigDecimal.ZERO, BigDecimal.ZERO,
                principalBalance, BigDecimal.ZERO, accumulatedInterest, BigDecimal.ZERO));

        for (LocalDate date : scheduleDates.subList(1, scheduleDates.size())) {
            long days = ChronoUnit.DAYS.between(previousDate, date);
            BigDecimal factor = BigDecimal.valueOf(Math.pow(1 + annualRate, (double) days / BASE_DAYS) - 1);
            BigDecimal provision = factor.multiply(principalBalance.add(accumulatedInterest), MC);

            BigDecimal paidInterest = BigDecimal.ZERO;
            BigDecimal amortization = BigDecimal.ZERO;
            String consolidated = "";

            if (paymentSet.contains(date)) {
                installmentNumber++;
                paidInterest = accumulatedInterest.add(provision);
                amortization = fixedAmortization;
                consolidated = installmentNumber + "/" + installmentQuantity;
            }

            accumulatedInterest = accumulatedInterest.add(provision).subtract(paidInterest);
            principalBalance = principalBalance.subtract(amortization);
            BigDecimal total = amortization.add(paidInterest);

            installmentRows.add(toRow(date, BigDecimal.ZERO, consolidated, total, amortization,
                    principalBalance, provision, accumulatedInterest, paidInterest));

            previousDate = date;
        }

        return installmentRows;
    }

    private LoanScheduleRow toRow(LocalDate date, BigDecimal loanAmount, String consolidated,
                                  BigDecimal total, BigDecimal amortization, BigDecimal principalBalance,
                                  BigDecimal provision, BigDecimal accumulatedInterest, BigDecimal paidInterest) {
        return new LoanScheduleRow(
                date,
                round(loanAmount),
                round(principalBalance.add(accumulatedInterest)),
                consolidated,
                round(total),
                round(amortization),
                round(principalBalance),
                round(provision),
                round(accumulatedInterest),
                round(paidInterest)
        );
    }

    private BigDecimal round(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
