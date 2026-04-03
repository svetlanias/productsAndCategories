package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dao.CategoryRepository;
import org.example.domain.Category;
import org.example.dto.CategoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(String name, Long parentId) {
        Category category = new Category();
        category.setName(name);

        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Родительская категория не найдена: " + parentId));
            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
        return buildCategoryDTO(saved);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        // Сначала мапим в DTO, заполняя ID и Name
        return categories.stream()
                .map(this::buildCategoryDTO)
                .collect(Collectors.toList());
    }

    private CategoryDTO buildCategoryDTO(Category cat) {
        return CategoryDTO.builder()
                .id(cat.getId())
                .name(cat.getName())
                .parentId(cat.getParent() != null ? cat.getParent().getId() : null)
                .parentName(cat.getParent() != null ? cat.getParent().getName() : null)
                .build();
    }
}
