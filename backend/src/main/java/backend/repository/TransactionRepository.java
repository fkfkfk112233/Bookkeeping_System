package backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
