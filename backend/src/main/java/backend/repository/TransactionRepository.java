package backend.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import backend.dto.dashboard.CategoryAmountResponse;
import backend.entity.Category;
import backend.entity.Transaction;
import backend.enums.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByCategory(Category category);

	@Query("""
	        SELECT COALESCE(SUM(t.amount), 0)
	        FROM Transaction t
	        WHERE t.type = :type
	        AND t.transactionDate BETWEEN :startDate AND :endDate
	        """)
	BigDecimal sumAmountByTypeAndDateBetween(
	        @Param("type") TransactionType type,
	        @Param("startDate") LocalDate startDate,
	        @Param("endDate") LocalDate endDate
	);
	
	@Query("""
	        SELECT new backend.dto.dashboard.CategoryAmountResponse(
	            c.id,
	            c.name,
	            SUM(t.amount)
	        )
	        FROM Transaction t
	        LEFT JOIN t.category c
	        WHERE t.type = :type
	        AND t.transactionDate BETWEEN :startDate AND :endDate
	        GROUP BY c.id, c.name
	        ORDER BY SUM(t.amount) DESC
	        """)
	List<CategoryAmountResponse> sumAmountGroupByCategory(
	        @Param("type") TransactionType type,
	        @Param("startDate") LocalDate startDate,
	        @Param("endDate") LocalDate endDate
	);
}
