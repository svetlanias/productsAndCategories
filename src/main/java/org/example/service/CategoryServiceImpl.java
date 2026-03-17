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

        // Создаём мапу id → name для быстрого поиска
        Map<Long, String> idToName = categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        return categories.stream()
                .map(cat -> {
                    CategoryDTO dto = buildCategoryDTO(cat);
                    if (cat.getParent() != null) {
                        dto.setParentName(idToName.get(cat.getParent().getId()));
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private CategoryDTO buildCategoryDTO(Category cat) {
        return new CategoryDTO(
                cat.getId(),
                cat.getName(),
                cat.getParent() != null ? cat.getParent().getId() : null,
                null
        );
    }
}