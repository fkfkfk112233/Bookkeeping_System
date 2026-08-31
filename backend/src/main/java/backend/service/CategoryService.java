package backend.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import backend.dto.category.CategoryRequest;
import backend.dto.category.CategoryResponse;
import backend.entity.Category;
import backend.entity.Transaction;
import backend.entity.User;
import backend.exception.ResourceNotFoundException;
import backend.repository.CategoryRepository;
import backend.repository.TransactionRepository;
import backend.repository.UserRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository,
            TransactionRepository transactionRepository,
            UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setType(category.getType());
        response.setDefaultCategory(category.getUser() == null);
        return response;
    }

    public List<CategoryResponse> getAllCategories() {
        User user = getCurrentUser();
        return categoryRepository.findByUserIsNullOrUserOrderByTypeAscNameAsc(user)
                .stream().map(this::toResponse).toList();
    }

    public CategoryResponse getCategoryById(Long id) {
        User user = getCurrentUser();
        Category category = categoryRepository.findByIdAndUser(id, user)
                .or(() -> categoryRepository.findByIdAndUserIsNull(id))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return toResponse(category);
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setUser(getCurrentUser());
        category.setName(request.getName());
        category.setType(request.getType());
        return toResponse(categoryRepository.save(category));
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        User user = getCurrentUser();
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Only your own categories can be modified"));
        category.setName(request.getName());
        category.setType(request.getType());
        return toResponse(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        User user = getCurrentUser();
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Only your own categories can be deleted"));

        List<Transaction> transactions = transactionRepository.findByCategory(category);
        for (Transaction transaction : transactions) {
            transaction.setCategory(null);
        }
        transactionRepository.saveAll(transactions);
        categoryRepository.delete(category);
    }
}
