package backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import backend.dto.category.CategoryRequest;
import backend.dto.category.CategoryResponse;
import backend.entity.Category;
import backend.entity.Transaction;
import backend.entity.User;
import backend.repository.CategoryRepository;
import backend.repository.TransactionRepository;
import backend.repository.UserRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public CategoryService(
            CategoryRepository categoryRepository,
            TransactionRepository transactionRepository,
            UserRepository userRepository) {

        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
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

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Test user not found"));

        Category category = new Category();

        category.setUser(user);
        category.setName(request.getName());
        category.setType(request.getType());

        Category savedCategory =
                categoryRepository.save(category);

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
