package backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import backend.dto.transaction.TransactionRequest;
import backend.dto.transaction.TransactionResponse;
import backend.entity.Category;
import backend.entity.Transaction;
import backend.entity.User;
import backend.exception.ResourceNotFoundException;
import backend.repository.CategoryRepository;
import backend.repository.TransactionRepository;
import backend.repository.UserRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Category getAccessibleCategory(Long categoryId, User user) {
        return categoryRepository.findByIdAndUser(categoryId, user)
                .or(() -> categoryRepository.findByIdAndUserIsNull(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    private void validateTransactionRequest(TransactionRequest request, Category category) {
        if (request.getAmount() == null
                || request.getAmount().compareTo(BigDecimal.TEN) < 0
                || request.getAmount().remainder(BigDecimal.TEN).compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("金額必須為 10 以上且為 10 的倍數");
        }
        if (request.getType() != category.getType()) {
            throw new IllegalArgumentException("交易類型與分類類型不一致");
        }
    }

    private TransactionResponse toResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        if (transaction.getCategory() != null) {
            response.setCategoryId(transaction.getCategory().getId());
            response.setCategoryName(transaction.getCategory().getName());
        }
        response.setType(transaction.getType());
        response.setAmount(transaction.getAmount());
        response.setPaymentMethod(transaction.getPaymentMethod());
        response.setDescription(transaction.getDescription());
        response.setTransactionDate(transaction.getTransactionDate());
        return response;
    }

    public List<TransactionResponse> getAllTransactions(LocalDate startDate, LocalDate endDate) {
        User user = getCurrentUser();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        return transactionRepository
                .findByUserAndTransactionDateGreaterThanEqualAndTransactionDateLessThanOrderByTransactionDateAsc(
                        user, startDateTime, endDateTime)
                .stream().map(this::toResponse).toList();
    }

    public TransactionResponse createTransaction(TransactionRequest request) {
        User user = getCurrentUser();
        Category category = getAccessibleCategory(request.getCategoryId(), user);
        validateTransactionRequest(request, category);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());

        return toResponse(transactionRepository.save(transaction));
    }

    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return toResponse(transaction);
    }

    public TransactionResponse updateTransaction(Long id, TransactionRequest request) {
        User user = getCurrentUser();
        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        Category category = getAccessibleCategory(request.getCategoryId(), user);
        validateTransactionRequest(request, category);

        transaction.setCategory(category);
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());

        return toResponse(transactionRepository.save(transaction));
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        transactionRepository.delete(transaction);
    }
}
