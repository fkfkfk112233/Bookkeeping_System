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
import backend.dto.dashboard.FrequencyResponse;
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

    // =========================
    // Summary
    // =========================

    public DashboardSummaryResponse getSummary(
            LocalDate startDate,
            LocalDate endDate) {

        LocalDateTime startDateTime =
                startDate.atStartOfDay();

        LocalDateTime endDateTime =
                endDate.plusDays(1).atStartOfDay();

        BigDecimal income =
                transactionRepository.sumAmountByTypeAndDateBetween(
                        TransactionType.INCOME,
                        startDateTime,
                        endDateTime
                );

        BigDecimal expense =
                transactionRepository.sumAmountByTypeAndDateBetween(
                        TransactionType.EXPENSE,
                        startDateTime,
                        endDateTime
                );

        BigDecimal balance =
                income.subtract(expense);

        return new DashboardSummaryResponse(
                income,
                expense,
                balance
        );
    }

    // =========================
    // Expense Category
    // =========================

    public List<CategoryAmountResponse> getExpense(
            LocalDate startDate,
            LocalDate endDate) {

        LocalDateTime startDateTime =
                startDate.atStartOfDay();

        LocalDateTime endDateTime =
                endDate.plusDays(1).atStartOfDay();

        List<CategoryAmountResponse> results =
                transactionRepository.sumAmountGroupByCategory(
                        TransactionType.EXPENSE,
                        startDateTime,
                        endDateTime
                );

        results.forEach(result -> {

            if (result.getCategoryId() == null) {
                result.setCategoryName("未分類");
            }

        });

        return results;
    }

    // =========================
    // Income Category
    // =========================

    public List<CategoryAmountResponse> getIncome(
            LocalDate startDate,
            LocalDate endDate) {

        LocalDateTime startDateTime =
                startDate.atStartOfDay();

        LocalDateTime endDateTime =
                endDate.plusDays(1).atStartOfDay();

        List<CategoryAmountResponse> results =
                transactionRepository.sumAmountGroupByCategory(
                        TransactionType.INCOME,
                        startDateTime,
                        endDateTime
                );

        results.forEach(result -> {

            if (result.getCategoryId() == null) {
                result.setCategoryName("未分類");
            }

        });

        return results;
    }

    // =========================
    // Determine Interval
    // =========================

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

    // =========================
    // Expense Trend
    // =========================

    public List<TrendResponse> getExpenseTrend(
            LocalDate startDate,
            LocalDate endDate) {

        StatisticsInterval interval =
                determineInterval(startDate, endDate);

        LocalDateTime startDateTime =
                startDate.atStartOfDay();

        LocalDateTime endDateTime =
                endDate.plusDays(1).atStartOfDay();

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

    // =========================
    // Fill Missing Trend
    // =========================

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

    // =========================
    // Income Trend
    // =========================

    public List<TrendResponse> getIncomeTrend(
            LocalDate startDate,
            LocalDate endDate) {

        StatisticsInterval interval =
                determineInterval(startDate, endDate);

        LocalDateTime startDateTime =
                startDate.atStartOfDay();

        LocalDateTime endDateTime =
                endDate.plusDays(1).atStartOfDay();

        List<Object[]> results;

        if (interval == StatisticsInterval.HOUR) {

            results = transactionRepository.getHourlyTrend(
                    TransactionType.INCOME.name(),
                    startDateTime,
                    endDateTime
            );

        } else if (interval == StatisticsInterval.DAY) {

            results = transactionRepository.getDailyTrend(
                    TransactionType.INCOME.name(),
                    startDateTime,
                    endDateTime
            );

        } else {

            results = transactionRepository.getMonthlyTrend(
                    TransactionType.INCOME.name(),
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

    // =========================
    // Frequency
    // =========================

    public List<FrequencyResponse> getFrequency(
            LocalDate startDate,
            LocalDate endDate) {

        StatisticsInterval interval =
                determineInterval(startDate, endDate);

        LocalDateTime startDateTime =
                startDate.atStartOfDay();

        LocalDateTime endDateTime =
                endDate.plusDays(1).atStartOfDay();

        List<Object[]> results;

        if (interval == StatisticsInterval.HOUR) {

            results = transactionRepository.getHourlyFrequency(
                    startDateTime,
                    endDateTime
            );

        } else if (interval == StatisticsInterval.DAY) {

            results = transactionRepository.getDailyFrequency(
                    startDateTime,
                    endDateTime
            );

        } else {

            results = transactionRepository.getMonthlyFrequency(
                    startDateTime,
                    endDateTime
            );
        }

        List<FrequencyResponse> response =
                new ArrayList<>();

        for (Object[] result : results) {

            String label = (String) result[0];

            Long count =
                    ((Number) result[1]).longValue();

            response.add(
                    new FrequencyResponse(
                            label,
                            count
                    )
            );
        }

        return fillMissingFrequencyIntervals(
                response,
                startDate,
                endDate,
                interval
        );
    }

    // =========================
    // Fill Missing Frequency
    // =========================

    private List<FrequencyResponse> fillMissingFrequencyIntervals(
            List<FrequencyResponse> existingResults,
            LocalDate startDate,
            LocalDate endDate,
            StatisticsInterval interval) {

        Map<String, Long> existingData =
                new HashMap<>();

        for (FrequencyResponse result : existingResults) {

            existingData.put(
                    result.getLabel(),
                    result.getCount()
            );
        }

        List<FrequencyResponse> response =
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

                Long count =
                        existingData.getOrDefault(
                                label,
                                0L
                        );

                response.add(
                        new FrequencyResponse(
                                label,
                                count
                        )
                );

                current = current.plusHours(1);
            }

        } else if (interval == StatisticsInterval.DAY) {

            LocalDate current = startDate;

            while (!current.isAfter(endDate)) {

                String label =
                        current.toString();

                Long count =
                        existingData.getOrDefault(
                                label,
                                0L
                        );

                response.add(
                        new FrequencyResponse(
                                label,
                                count
                        )
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

                Long count =
                        existingData.getOrDefault(
                                label,
                                0L
                        );

                response.add(
                        new FrequencyResponse(
                                label,
                                count
                        )
                );

                current = current.plusMonths(1);
            }
        }

        return response;
    }
}