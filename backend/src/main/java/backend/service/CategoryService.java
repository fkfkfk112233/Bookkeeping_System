package backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import backend.dto.category.CategoryRequest;
import backend.dto.category.CategoryResponse;
import backend.entity.Category;
import backend.entity.Transaction;
import backend.repository.CategoryRepository;
import backend.repository.TransactionRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public CategoryService(
            CategoryRepository categoryRepository,
            TransactionRepository transactionRepository) {

        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }
    
    private CategoryResponse toResponse(Category category) {

        CategoryResponse response = new CategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());
        response.setType(category.getType());

        return response;
    }
    
    public List<CategoryResponse> getAllCategories() {

        List<Category> categories =
                categoryRepository.findAll();

        return categories.stream()
                .map(this::toResponse)
                .toList();
    }
    
    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return toResponse(category);
    }

    public CategoryResponse createCategory(CategoryRequest request) {

        Category category = new Category();

        category.setName(request.getName());
        category.setType(request.getType());

        Category savedCategory =
                categoryRepository.save(category);

        CategoryResponse response = new CategoryResponse();

        response.setId(savedCategory.getId());
        response.setName(savedCategory.getName());
        response.setType(savedCategory.getType());

        return toResponse(savedCategory);
    }
    
    public CategoryResponse updateCategory(
            Long id,
            CategoryRequest request) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());
        category.setType(request.getType());

        Category updatedCategory =
                categoryRepository.save(category);

        return toResponse(updatedCategory);
    }
    
    public void deleteCategory(Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<Transaction> transactions =
                transactionRepository.findByCategory(category);

        for (Transaction transaction : transactions) {
            transaction.setCategory(null);
        }

        transactionRepository.saveAll(transactions);

        categoryRepository.delete(category);
    }
}
