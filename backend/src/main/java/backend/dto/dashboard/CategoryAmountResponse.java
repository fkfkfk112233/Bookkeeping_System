package backend.dto.dashboard;

import java.math.BigDecimal;

public class CategoryAmountResponse {

    private Long categoryId;

    private String categoryName;

    private BigDecimal amount;

    public CategoryAmountResponse() {
    }

    public CategoryAmountResponse(
            Long categoryId,
            String categoryName,
            BigDecimal amount) {

        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.amount = amount;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
