package backend.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import backend.enums.PaymentMethod;
import backend.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransactionRequest {

    @NotNull
    private Long categoryId;

    @NotBlank
    private TransactionType type;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotBlank
    private PaymentMethod paymentMethod;

    private String description;

    @NotNull
    private LocalDate transactionDate;

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

    public void setType(@NotBlank TransactionType type) {
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

    public void setPaymentMethod(@NotBlank PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}