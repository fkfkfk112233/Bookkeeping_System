package backend.dto.category;

import backend.enums.TransactionType;

public class CategoryResponse {

    private Long id;

    private String name;

    private TransactionType type;

    public CategoryResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType transactionType) {
        this.type = transactionType;
    }
}
