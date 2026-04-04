package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Запрос для фильтрации товаров с фасетами
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFilterRequest {

    private Long categoryId;

    // Фильтры по точным значениям (для string, boolean, enum)
    private Map<String, List<String>> exactFilters;

    // Фильтры по диапазону (для number)
    private Map<String, PriceRange> priceRanges;

    // Поиск по тексту в названии или описании
    private String searchQuery;

    // Сортировка
    private String sortBy;
    private SortDirection sortDirection;

    // Пагинация
    private Integer page;
    private Integer size;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PriceRange {
        private BigDecimal min;
        private BigDecimal max;
    }

    public enum SortDirection {
        ASC, DESC
    }
}
