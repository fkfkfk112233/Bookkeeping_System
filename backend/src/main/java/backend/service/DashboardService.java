package backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import backend.dto.dashboard.CategoryAmountResponse;
import backend.dto.dashboard.DashboardSummaryResponse;
import backend.dto.dashboard.TrendResponse;
import backend.enums.StatisticsInterval;
import backend.enums.TransactionType;
import backend.repository.TransactionRepository;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardService(
            TransactionRepository transactionRepository) {

        this.transactionRepository = transactionRepository;
    }

    public DashboardSummaryResponse getSummary(
            LocalDate startDate,
            LocalDate endDate) {

        BigDecimal income =
                transactionRepository.sumAmountByTypeAndDateBetween(
                        TransactionType.INCOME,
                        startDate,
                        endDate
                );

        BigDecimal expense =
                transactionRepository.sumAmountByTypeAndDateBetween(
                        TransactionType.EXPENSE,
                        startDate,
                        endDate
                );

        BigDecimal balance =
                income.subtract(expense);

        return new DashboardSummaryResponse(
                income,
                expense,
                balance
        );
    }
    
    public List<CategoryAmountResponse> getExpense(
            LocalDate startDate,
            LocalDate endDate) {

        List<CategoryAmountResponse> results =
                transactionRepository.sumAmountGroupByCategory(
                        TransactionType.EXPENSE,
                        startDate,
                        endDate
                );

        results.forEach(result -> {

            if (result.getCategoryId() == null) {
                result.setCategoryName("未分類");
            }

        });

        return results;
    }
    
    public List<CategoryAmountResponse> getIncome(
            LocalDate startDate,
            LocalDate endDate) {

        List<CategoryAmountResponse> results =
                transactionRepository.sumAmountGroupByCategory(
                        TransactionType.INCOME,
                        startDate,
                        endDate
                );

        results.forEach(result -> {

            if (result.getCategoryId() == null) {
                result.setCategoryName("未分類");
            }

        });

        return results;
    }
    
    private StatisticsInterval determineInterval(
            LocalDate startDate,
            LocalDate endDate) {

        long days =
                java.time.temporal.ChronoUnit.DAYS.between(
                        startDate,
                        endDate
                ) + 1;

        if (days == 1) {
            return StatisticsInterval.HOUR;
        }

        if (days <= 31) {
            return StatisticsInterval.DAY;
        }

        return StatisticsInterval.MONTH;
    }
    
    public List<TrendResponse> getExpenseTrend(
            LocalDate startDate,
            LocalDate endDate) {

        StatisticsInterval interval =
                determineInterval(startDate, endDate);

        LocalDateTime startDateTime =
                startDate.atStartOfDay();

        LocalDateTime endDateTime =
                endDate.atTime(23, 59, 59);

        List<Object[]> results;

        if (interval == StatisticsInterval.HOUR) {

            results = transactionRepository.getHourlyTrend(
                    TransactionType.EXPENSE.name(),
                    startDateTime,
                    endDateTime
            );

        } else if (interval == StatisticsInterval.DAY) {

            results = transactionRepository.getDailyTrend(
                    TransactionType.EXPENSE.name(),
                    startDateTime,
                    endDateTime
            );

        } else {

            results = transactionRepository.getMonthlyTrend(
                    TransactionType.EXPENSE.name(),
                    startDateTime,
                    endDateTime
            );
        }

        List<TrendResponse> response = new ArrayList<>();

        for (Object[] result : results) {

            String label = (String) result[0];

            BigDecimal amount =
                    new BigDecimal(result[1].toString());

            response.add(
                    new TrendResponse(label, amount)
            );
        }

        return fillMissingIntervals(
                response,
                startDate,
                endDate,
                interval
        );
    }
    
    private List<TrendResponse> fillMissingIntervals(
            List<TrendResponse> existingResults,
            LocalDate startDate,
            LocalDate endDate,
            StatisticsInterval interval) {

        Map<String, BigDecimal> existingData =
                new HashMap<>();

        for (TrendResponse result : existingResults) {

            existingData.put(
                    result.getLabel(),
                    result.getAmount()
            );
        }

        List<TrendResponse> response =
                new ArrayList<>();

        if (interval == StatisticsInterval.HOUR) {

            LocalDateTime current =
                    startDate.atStartOfDay();

            LocalDateTime end =
                    endDate.atTime(23, 0);

            while (!current.isAfter(end)) {

                String label =
                        current.format(
                                DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd HH:00"
                                )
                        );

                BigDecimal amount =
                        existingData.getOrDefault(
                                label,
                                BigDecimal.ZERO
                        );

                response.add(
                        new TrendResponse(label, amount)
                );

                current = current.plusHours(1);
            }

        } else if (interval == StatisticsInterval.DAY) {

            LocalDate current = startDate;

            while (!current.isAfter(endDate)) {

                String label =
                        current.toString();

                BigDecimal amount =
                        existingData.getOrDefault(
                                label,
                                BigDecimal.ZERO
                        );

                response.add(
                        new TrendResponse(label, amount)
                );

                current = current.plusDays(1);
            }

        } else {

            YearMonth current =
                    YearMonth.from(startDate);

            YearMonth end =
                    YearMonth.from(endDate);

            while (!current.isAfter(end)) {

                String label =
                        current.toString();

                BigDecimal amount =
                        existingData.getOrDefault(
                                label,
                                BigDecimal.ZERO
                        );

                response.add(
                        new TrendResponse(label, amount)
                );

                current = current.plusMonths(1);
            }
        }

        return response;
    }
}