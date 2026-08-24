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

    // =========================
    // Summary
    // =========================

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.type = :type
            AND t.transactionDate >= :startDate
            AND t.transactionDate < :endDate
            """)
    BigDecimal sumAmountByTypeAndDateBetween(
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
            WHERE t.type = :type
            AND t.transactionDate >= :startDate
            AND t.transactionDate < :endDate
            GROUP BY c.id, c.name
            ORDER BY SUM(t.amount) DESC
            """)
    List<CategoryAmountResponse> sumAmountGroupByCategory(
            @Param("type") TransactionType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // =========================
    // Trend
    // =========================

    List<Transaction>
    findByTypeAndTransactionDateGreaterThanEqualAndTransactionDateLessThanOrderByTransactionDateAsc(
            TransactionType type,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // =========================
    // Frequency
    // =========================

    List<Transaction>
    findByTransactionDateGreaterThanEqualAndTransactionDateLessThanOrderByTransactionDateAsc(
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}