package backend.service;

import org.springframework.stereotype.Service;

import backend.dto.category.CategoryRequest;
import backend.dto.category.CategoryResponse;
import backend.entity.Category;
import backend.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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

        return response;
    }
}
