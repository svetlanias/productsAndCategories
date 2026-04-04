package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Ответ с фасетами для фильтрации
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacetResponse {

    // Список доступных значений для каждого атрибута с количеством товаров
    private Map<String, List<FacetValue>> facets;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FacetValue {
        private String value;
        private Long count;
    }
}
