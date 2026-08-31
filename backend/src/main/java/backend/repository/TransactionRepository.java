package backend.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import backend.dto.dashboard.CategoryAmountResponse;
import backend.entity.Category;
import backend.entity.Transaction;
import backend.entity.User;
import backend.enums.TransactionType;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCategory(Category category);

    boolean existsByUser(User user);

    List<Transaction> findByUserAndTransactionDateGreaterThanEqualAndTransactionDateLessThanOrderByTransactionDateAsc(
            User user, LocalDateTime startDate, LocalDateTime endDate);

    java.util.Optional<Transaction> findByIdAndUser(Long id, User user);

    // =========================
    // Summary
    // =========================

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.user = :user
            AND t.type = :type
            AND t.transactionDate >= :startDate
            AND t.transactionDate < :endDate
            """)
    BigDecimal sumAmountByTypeAndDateBetween(
            @Param("user") User user,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // =========================
    // Category Statistics
    // =========================

    @Query("""
            SELECT new backend.dto.dashboard.CategoryAmountResponse(
                c.id,
                c.name,
                SUM(t.amount)
            )
            FROM Transaction t
            LEFT JOIN t.category c
            WHERE t.user = :user
            AND t.type = :type
            AND t.transactionDate >= :startDate
            AND t.transactionDate < :endDate
            GROUP BY c.id, c.name
            ORDER BY SUM(t.amount) DESC
            """)
    List<CategoryAmountResponse> sumAmountGroupByCategory(
            @Param("user") User user,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // =========================
    // Trend
    // =========================

    List<Transaction>
    findByUserAndTypeAndTransactionDateGreaterThanEqualAndTransactionDateLessThanOrderByTransactionDateAsc(
            User user, TransactionType type, LocalDateTime startDate, LocalDateTime endDate
    );

    // =========================
    // Frequency
    // =========================

    List<Transaction>
    findByUserAndTransactionDateGreaterThanEqualAndTransactionDateLessThanOrderByTransactionDateAsc(
            User user, LocalDateTime startDate, LocalDateTime endDate
    );
}