package backend.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import backend.enums.PaymentMethod;
import backend.enums.TransactionType;

public class TransactionRequest {

    @NotNull
    private Long categoryId;

    @NotNull
    private TransactionType type;

    @NotNull
    @DecimalMin(value = "10.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal amount;

    @NotNull
    private PaymentMethod paymentMethod;

    private String description;

    @NotNull
    private LocalDateTime transactionDate;

    public TransactionRequest() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(@NotNull LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}