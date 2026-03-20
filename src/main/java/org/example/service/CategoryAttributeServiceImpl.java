package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dao.AttributeRepository;
import org.example.dao.CategoryAttributeRepository;
import org.example.dao.CategoryRepository;
import org.example.domain.Attribute;
import org.example.domain.Category;
import org.example.domain.CategoryAttribute;
import org.example.dto.CategoryAttributeDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryAttributeServiceImpl implements CategoryAttributeService {

    private final CategoryAttributeRepository categoryAttributeRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;

    public CategoryAttributeServiceImpl(
            CategoryAttributeRepository categoryAttributeRepository,
            CategoryRepository categoryRepository,
            AttributeRepository attributeRepository) {
        this.categoryAttributeRepository = categoryAttributeRepository;
        this.categoryRepository = categoryRepository;
        this.attributeRepository = attributeRepository;
    }

    @Override
    public List<CategoryAttributeDTO> getAttributesByCategoryId(Long categoryId) {
        return categoryAttributeRepository.findByCategoryId(categoryId).stream()
                .map(ca -> new CategoryAttributeDTO(
                        ca.getAttribute().getId(),
                        ca.getAttribute().getName(),
                        ca.isRequired()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addAttributeToCategory(Long categoryId, Long attributeId, boolean required) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new RuntimeException("Атрибут не найден"));

        // Проверяем, не привязан ли уже
        if (categoryAttributeRepository.existsByCategoryIdAndAttributeId(categoryId, attributeId)) {
            throw new RuntimeException("Атрибут уже привязан к категории");
        }

        CategoryAttribute ca = new CategoryAttribute();
        ca.setCategory(category);
        ca.setAttribute(attribute);
        ca.setRequired(required);

        categoryAttributeRepository.save(ca);
    }
}
