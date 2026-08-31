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
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import backend.dto.dashboard.CategoryAmountResponse;
import backend.dto.dashboard.DashboardSummaryResponse;
import backend.dto.dashboard.FrequencyResponse;
import backend.dto.dashboard.TrendResponse;
import backend.enums.StatisticsInterval;
import backend.enums.TransactionType;
import backend.repository.TransactionRepository;
import backend.repository.UserRepository;
import backend.entity.User;
import backend.entity.Transaction;

@Service
public class DashboardService {

	private final TransactionRepository transactionRepository;
	private final UserRepository userRepository;

	public DashboardService(TransactionRepository transactionRepository, UserRepository userRepository) {

		this.transactionRepository = transactionRepository;
		this.userRepository = userRepository;
	}

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	// =========================
	// Summary
	// =========================

	public DashboardSummaryResponse getSummary(LocalDate startDate, LocalDate endDate) {

		LocalDateTime startDateTime = startDate.atStartOfDay();

		LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

		BigDecimal income = transactionRepository.sumAmountByTypeAndDateBetween(getCurrentUser(), TransactionType.INCOME, startDateTime,
				endDateTime);

		BigDecimal expense = transactionRepository.sumAmountByTypeAndDateBetween(getCurrentUser(), TransactionType.EXPENSE, startDateTime,
				endDateTime);

		BigDecimal balance = income.subtract(expense);

		return new DashboardSummaryResponse(income, expense, balance);
	}

	// =========================
	// Expense Category
	// =========================

	public List<CategoryAmountResponse> getExpense(LocalDate startDate, LocalDate endDate) {

		LocalDateTime startDateTime = startDate.atStartOfDay();

		LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

		List<CategoryAmountResponse> results = transactionRepository.sumAmountGroupByCategory(getCurrentUser(), TransactionType.EXPENSE,
				startDateTime, endDateTime);

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

	public List<CategoryAmountResponse> getIncome(LocalDate startDate, LocalDate endDate) {

		LocalDateTime startDateTime = startDate.atStartOfDay();

		LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

		List<CategoryAmountResponse> results = transactionRepository.sumAmountGroupByCategory(getCurrentUser(), TransactionType.INCOME,
				startDateTime, endDateTime);

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

	private StatisticsInterval determineInterval(LocalDate startDate, LocalDate endDate) {

		long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

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

	public List<TrendResponse> getExpenseTrend(LocalDate startDate, LocalDate endDate) {

		StatisticsInterval interval = determineInterval(startDate, endDate);

		LocalDateTime startDateTime = startDate.atStartOfDay();

		LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

		List<Transaction> transactions = transactionRepository
				.findByUserAndTypeAndTransactionDateGreaterThanEqualAndTransactionDateLessThanOrderByTransactionDateAsc(
						getCurrentUser(), TransactionType.EXPENSE, startDateTime, endDateTime);

		List<TrendResponse> response = groupTransactionsByInterval(transactions, interval);

		return fillMissingIntervals(response, startDate, endDate, interval);
	}

	// =========================
	// Fill Missing Trend
	// =========================

	private List<TrendResponse> fillMissingIntervals(List<TrendResponse> existingResults, LocalDate startDate,
			LocalDate endDate, StatisticsInterval interval) {

		Map<String, BigDecimal> existingData = new HashMap<>();

		for (TrendResponse result : existingResults) {

			existingData.put(result.getLabel(), result.getAmount());
		}

		List<TrendResponse> response = new ArrayList<>();

		if (interval == StatisticsInterval.HOUR) {

			LocalDateTime current = startDate.atStartOfDay();

			LocalDateTime end = endDate.atTime(23, 0);

			while (!current.isAfter(end)) {

				String label = current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00"));

				BigDecimal amount = existingData.getOrDefault(label, BigDecimal.ZERO);

				response.add(new TrendResponse(label, amount));

				current = current.plusHours(1);
			}

		} else if (interval == StatisticsInterval.DAY) {

			LocalDate current = startDate;

			while (!current.isAfter(endDate)) {

				String label = current.toString();

				BigDecimal amount = existingData.getOrDefault(label, BigDecimal.ZERO);

				response.add(new TrendResponse(label, amount));

				current = current.plusDays(1);
			}

		} else {

			YearMonth current = YearMonth.from(startDate);

			YearMonth end = YearMonth.from(endDate);

			while (!current.isAfter(end)) {

				String label = current.toString();

				BigDecimal amount = existingData.getOrDefault(label, BigDecimal.ZERO);

				response.add(new TrendResponse(label, amount));

				current = current.plusMonths(1);
			}
		}

		return response;
	}

	// =========================
	// Income Trend
	// =========================

	public List<TrendResponse> getIncomeTrend(LocalDate startDate, LocalDate endDate) {

		StatisticsInterval interval = determineInterval(startDate, endDate);

		LocalDateTime startDateTime = startDate.atStartOfDay();

		LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

		List<Transaction> transactions = transactionRepository
				.findByUserAndTypeAndTransactionDateGreaterThanEqualAndTransactionDateLessThanOrderByTransactionDateAsc(
						getCurrentUser(), TransactionType.INCOME, startDateTime, endDateTime);

		List<TrendResponse> response = groupTransactionsByInterval(transactions, interval);

		return fillMissingIntervals(response, startDate, endDate, interval);
	}

	// ==========================
	// Trend Method
	// ==========================

	private List<TrendResponse> groupTransactionsByInterval(List<Transaction> transactions,
			StatisticsInterval interval) {

		Map<String, BigDecimal> grouped = new HashMap<>();

		DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00");

		for (Transaction transaction : transactions) {

			LocalDateTime dateTime = transaction.getTransactionDate();

			String label;

			if (interval == StatisticsInterval.HOUR) {

				label = dateTime.format(hourFormatter);

			} else if (interval == StatisticsInterval.DAY) {

				label = dateTime.toLocalDate().toString();

			} else {

				label = YearMonth.from(dateTime).toString();
			}

			grouped.merge(label, transaction.getAmount(), BigDecimal::add);
		}

		List<TrendResponse> response = new ArrayList<>();

		for (Map.Entry<String, BigDecimal> entry : grouped.entrySet()) {

			response.add(new TrendResponse(entry.getKey(), entry.getValue()));
		}

		response.sort((a, b) -> a.getLabel().compareTo(b.getLabel()));

		return response;
	}

	// =========================
	// Frequency
	// =========================

	public List<FrequencyResponse> getFrequency(LocalDate startDate, LocalDate endDate) {

		StatisticsInterval interval = determineInterval(startDate, endDate);

		LocalDateTime startDateTime = startDate.atStartOfDay();

		LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

		List<Transaction> transactions = transactionRepository
				.findByUserAndTransactionDateGreaterThanEqualAndTransactionDateLessThanOrderByTransactionDateAsc(getCurrentUser(), startDateTime,
						endDateTime);

		Map<String, Long> grouped = new HashMap<>();

		DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00");

		for (Transaction transaction : transactions) {

			LocalDateTime dateTime = transaction.getTransactionDate();

			String label;

			if (interval == StatisticsInterval.HOUR) {

				label = dateTime.format(hourFormatter);

			} else if (interval == StatisticsInterval.DAY) {

				label = dateTime.toLocalDate().toString();

			} else {

				label = YearMonth.from(dateTime).toString();
			}

			grouped.merge(label, 1L, Long::sum);
		}

		List<FrequencyResponse> response = new ArrayList<>();

		for (Map.Entry<String, Long> entry : grouped.entrySet()) {

			response.add(new FrequencyResponse(entry.getKey(), entry.getValue()));
		}

		response.sort((a, b) -> a.getLabel().compareTo(b.getLabel()));

		return fillMissingFrequencyIntervals(response, startDate, endDate, interval);
	}

	// =========================
	// Fill Missing Frequency
	// =========================

	private List<FrequencyResponse> fillMissingFrequencyIntervals(List<FrequencyResponse> existingResults,
			LocalDate startDate, LocalDate endDate, StatisticsInterval interval) {

		Map<String, Long> existingData = new HashMap<>();

		for (FrequencyResponse result : existingResults) {

			existingData.put(result.getLabel(), result.getCount());
		}

		List<FrequencyResponse> response = new ArrayList<>();

		if (interval == StatisticsInterval.HOUR) {

			LocalDateTime current = startDate.atStartOfDay();

			LocalDateTime end = endDate.atTime(23, 0);

			while (!current.isAfter(end)) {

				String label = current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00"));

				Long count = existingData.getOrDefault(label, 0L);

				response.add(new FrequencyResponse(label, count));

				current = current.plusHours(1);
			}

		} else if (interval == StatisticsInterval.DAY) {

			LocalDate current = startDate;

			while (!current.isAfter(endDate)) {

				String label = current.toString();

				Long count = existingData.getOrDefault(label, 0L);

				response.add(new FrequencyResponse(label, count));

				current = current.plusDays(1);
			}

		} else {

			YearMonth current = YearMonth.from(startDate);

			YearMonth end = YearMonth.from(endDate);

			while (!current.isAfter(end)) {

				String label = current.toString();

				Long count = existingData.getOrDefault(label, 0L);

				response.add(new FrequencyResponse(label, count));

				current = current.plusMonths(1);
			}
		}

		return response;
	}
}