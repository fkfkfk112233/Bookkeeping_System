package backend.dto.category;

import backend.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {

    @NotBlank
    private String name;

    @NotBlank
    private TransactionType type;

    public CategoryRequest() {
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

    public void setType(@NotBlank TransactionType type) {
        this.type = type;
    }
}