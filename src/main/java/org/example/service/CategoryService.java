package org.example.service;

import org.example.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(String name, Long parentId);
    List<CategoryDTO> getAllCategories();
}