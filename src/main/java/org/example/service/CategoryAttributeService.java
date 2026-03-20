package org.example.service;

import org.example.dto.CategoryAttributeDTO;

import java.util.List;

public interface CategoryAttributeService {
    List<CategoryAttributeDTO> getAttributesByCategoryId(Long categoryId);
    void addAttributeToCategory(Long categoryId, Long attributeId, boolean required);
}
