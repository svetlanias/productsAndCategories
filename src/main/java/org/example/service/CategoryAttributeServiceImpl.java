package org.example.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.AttributeRepository;
import org.example.dao.CategoryAttributeRepository;
import org.example.dao.CategoryRepository;
import org.example.domain.Attribute;
import org.example.domain.Category;
import org.example.domain.CategoryAttribute;
import org.example.dto.CategoryAttributeDTO;
import org.example.service.CategoryAttributeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryAttributeServiceImpl implements CategoryAttributeService {

    private final CategoryAttributeRepository categoryAttributeRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;

    @Override
    public List<CategoryAttributeDTO> getAttributesByCategoryId(Long categoryId) {
        log.info("Получение атрибутов для категории: {}", categoryId);

        // Проверка существования категории
        if (!categoryRepository.existsById(categoryId)) {
            log.error("Категория не найдена: {}", categoryId);
            throw new IllegalArgumentException("Категория с ID " + categoryId + " не найдена");
        }

        return categoryAttributeRepository.findByCategoryId(categoryId).stream()
                .map(ca -> CategoryAttributeDTO.builder()
                        .attributeId(ca.getAttribute().getId())
                        .attributeName(ca.getAttribute().getName())
                        .required(ca.isRequired())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addAttributeToCategory(Long categoryId, Long attributeId, boolean required) {
        log.info("Добавление атрибута {} к категории {}", attributeId, categoryId);

        // Проверка существования категории
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Категория не найдена: {}", categoryId);
                    return new IllegalArgumentException("Категория с ID " + categoryId + " не найдена");
                });

        // Проверка существования атрибута
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> {
                    log.error("Атрибут не найден: {}", attributeId);
                    return new IllegalArgumentException("Атрибут с ID " + attributeId + " не найден");
                });

        // Проверка дубликата
        if (categoryAttributeRepository.existsByCategoryIdAndAttributeId(categoryId, attributeId)) {
            log.warn("Атрибут {} уже привязан к категории {}", attributeId, categoryId);
            throw new IllegalArgumentException("Атрибут уже привязан к этой категории");
        }

        // Создание связи с правильным конструктором
        CategoryAttribute.CategoryAttributeId id = new CategoryAttribute.CategoryAttributeId(categoryId, attributeId);

        CategoryAttribute ca = CategoryAttribute.builder()
                .id(id)
                .category(category)
                .attribute(attribute)
                .required(required)
                .build();

        categoryAttributeRepository.save(ca);
        log.info("Атрибут {} успешно добавлен к категории {}", attributeId, categoryId);
    }

    @Override
    @Transactional
    public void removeAttributeFromCategory(Long categoryId, Long attributeId) {
        log.info("Удаление атрибута {} из категории {}", attributeId, categoryId);

        CategoryAttribute.CategoryAttributeId id = new CategoryAttribute.CategoryAttributeId(categoryId, attributeId);

        if (!categoryAttributeRepository.existsById(id)) {
            log.warn("Связь не найдена: категория {}, атрибут {}", categoryId, attributeId);
            throw new IllegalArgumentException("Связь между категорией и атрибутом не найдена");
        }

        categoryAttributeRepository.deleteById(id);
        log.info("Атрибут {} успешно удален из категории {}", attributeId, categoryId);
    }

    @Override
    @Transactional
    public void updateAttributeRequired(Long categoryId, Long attributeId, boolean required) {
        log.info("Обновление флага required для атрибута {} в категории {}: {}",
                attributeId, categoryId, required);

        CategoryAttribute ca = categoryAttributeRepository.findByCategoryIdAndAttributeId(categoryId, attributeId)
                .orElseThrow(() -> {
                    log.error("Связь не найдена: категория {}, атрибут {}", categoryId, attributeId);
                    return new IllegalArgumentException("Связь между категорией и атрибутом не найдена");
                });

        ca.setRequired(required);
        categoryAttributeRepository.save(ca);
        log.info("Флаг required успешно обновлен");
    }
}


