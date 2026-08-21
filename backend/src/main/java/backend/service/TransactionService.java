package backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import backend.dto.transaction.TransactionRequest;
import backend.dto.transaction.TransactionResponse;
import backend.entity.Category;
import backend.entity.Transaction;
import backend.exception.ResourceNotFoundException;
import backend.repository.CategoryRepository;
import backend.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            CategoryRepository categoryRepository) {

        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }
    
    private TransactionResponse toResponse(Transaction transaction) {

        TransactionResponse response = new TransactionResponse();

        response.setId(transaction.getId());
        response.setCategoryId(
                transaction.getCategory().getId());
        response.setCategoryName(
                transaction.getCategory().getName());
        response.setType(transaction.getType());
        response.setAmount(transaction.getAmount());
        response.setPaymentMethod(
                transaction.getPaymentMethod());
        response.setDescription(
                transaction.getDescription());
        response.setTransactionDate(
                transaction.getTransactionDate());

        return response;
    }
    
    public List<TransactionResponse> getAllTransactions() {

        List<Transaction> transactions =
                transactionRepository.findAll();

        return transactions.stream()
                .map(this::toResponse)
                .toList();
    }
    
    public TransactionResponse createTransaction(
            TransactionRequest request) {

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transaction transaction = new Transaction();

        transaction.setCategory(category);
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        TransactionResponse response = new TransactionResponse();

        response.setId(savedTransaction.getId());
        response.setCategoryId(
                savedTransaction.getCategory().getId());
        response.setCategoryName(
                savedTransaction.getCategory().getName());
        response.setType(savedTransaction.getType());
        response.setAmount(savedTransaction.getAmount());
        response.setPaymentMethod(
                savedTransaction.getPaymentMethod());
        response.setDescription(
                savedTransaction.getDescription());
        response.setTransactionDate(
                savedTransaction.getTransactionDate());

        return toResponse(savedTransaction);
    }
    
    public TransactionResponse getTransactionById(Long id) {

        Transaction transaction = transactionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        return toResponse(transaction);
    }
    
    public TransactionResponse updateTransaction(
            Long id,
            TransactionRequest request) {

        Transaction transaction = transactionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        transaction.setCategory(category);
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());

        Transaction updatedTransaction =
                transactionRepository.save(transaction);

        return toResponse(updatedTransaction);
    }
    
    public void deleteTransaction(Long id) {

        Transaction transaction = transactionRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transactionRepository.delete(transaction);
    }
}