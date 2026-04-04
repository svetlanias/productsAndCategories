package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO для фильтра в запросе фильтрации товаров
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Filter {

    /**
     * Ключ атрибута (например, "Тип основы", "Вязкость")
     */
    private String attributeKey;

    /**
     * Оператор сравнения: EQ, IN, GT, LT, GTE, LTE, LIKE
     */
    private String operator;

    /**
     * Значения для фильтрации
     */
    private List<String> values;
}