package org.example.service;

import org.example.dto.CategoryAttributeDTO;

import java.util.List;

public interface CategoryAttributeService {

    /**
     * Получить все атрибуты категории
     */
    List<CategoryAttributeDTO> getAttributesByCategoryId(Long categoryId);

    /**
     * Добавить атрибут к категории
     */
    void addAttributeToCategory(Long categoryId, Long attributeId, boolean required);

    /**
     * Удалить атрибут из категории
     */
    void removeAttributeFromCategory(Long categoryId, Long attributeId);

    /**
     * Обновить флаг "обязательный"
     */
    void updateAttributeRequired(Long categoryId, Long attributeId, boolean required);
}
