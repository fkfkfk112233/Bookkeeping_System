package backend.service;

import org.springframework.stereotype.Service;

import backend.dto.transaction.TransactionRequest;
import backend.dto.transaction.TransactionResponse;
import backend.entity.Category;
import backend.entity.Transaction;
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

        return response;
    }
}