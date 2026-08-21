package backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import backend.dto.dashboard.CategoryAmountResponse;
import backend.dto.dashboard.DashboardSummaryResponse;
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
}