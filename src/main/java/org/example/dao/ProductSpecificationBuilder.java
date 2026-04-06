package org.example.dao;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.example.domain.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ProductSpecificationBuilder {

    /**
     * Построение спецификации для фильтрации товаров
     */
    public Specification<Product> buildFilterSpecification(
            Long categoryId,
            Map<String, List<String>> exactFilters,
            Map<String, PriceRange> priceRanges,
            String searchQuery
    ) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // Фильтр по категории
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            // Только активные товары
            predicates.add(criteriaBuilder.isTrue(root.get("active")));

            // Поиск по тексту (name, description, maker)
            if (searchQuery != null && !searchQuery.isBlank()) {
                String searchPattern = "%" + searchQuery.toLowerCase() + "%";
                var namePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), searchPattern);
                var descPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")), searchPattern);
                var makerPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("maker")), searchPattern);
                predicates.add(criteriaBuilder.or(namePredicate, descPredicate, makerPredicate));
            }

            // Точные фильтры по атрибутам (JSONB)
            if (exactFilters != null && !exactFilters.isEmpty()) {
                for (Map.Entry<String, List<String>> entry : exactFilters.entrySet()) {
                    String attributeName = entry.getKey();
                    List<String> values = entry.getValue();

                    if (values != null && !values.isEmpty()) {
                        // Проверка: атрибут существует И его значение в списке
                        var jsonPath = root.get("attributes");

                        // Создаем условие: attributes->>'attrName' IN ('val1', 'val2', ...)
                        // Для PostgreSQL JSONB используем function jsonb_extract_path_text
                        var attributeValue = criteriaBuilder.function(
                                "jsonb_extract_path_text",
                                String.class,
                                jsonPath,
                                criteriaBuilder.literal(attributeName)
                        );

                        var inPredicate = attributeValue.in(Arrays.copyOf(values.toArray(new String[0]), values.size(), Object[].class));
                        predicates.add(inPredicate);
                    }
                }
            }

            // Диапазоны числовых атрибутов
            	            if (priceRanges != null && !priceRanges.isEmpty()) {
                	                for (Map.Entry<String, PriceRange> entry : priceRanges.entrySet()) {
                    	                    String attributeName = entry.getKey();
                    	                    PriceRange range = entry.getValue();

                    	                    if (range == null) continue;

                    	                    // Специальная обработка для основного поля цены
                    	                    if ("price".equals(attributeName)) {
                        	                        Path<BigDecimal> pricePath = root.get("price");
                        	                        if (range.getMin() != null) {
                            	                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(pricePath, range.getMin()));
                            	                        }
                        	                        if (range.getMax() != null) {
                            	                            predicates.add(criteriaBuilder.lessThanOrEqualTo(pricePath, range.getMax()));
                            	                        }
                        	                    } else {
                        	                        // Обработка для числовых атрибутов внутри JSON (если понадобятся)
                        	                        var jsonPath = root.get("attributes");
                        	                        var attributeValue = criteriaBuilder.function(
                                	                                "jsonb_extract_path_text",
                                	                                String.class,
                                	                                jsonPath,
                                	                                criteriaBuilder.literal(attributeName)
                                	                        );

                        	                        // Преобразуем текст в numeric для сравнения
                        	                        // Исправлено использование CAST для совместимости с Hibernate 6+
                        	                        var numericValue = criteriaBuilder.function(
                                	                                "cast",
                        	                                BigDecimal.class,
                        	                                attributeValue,
                        	                                criteriaBuilder.literal("numeric")
                        	                        );

                        	                        if (range.getMin() != null) {
                        	                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(numericValue, range.getMin()));
                                                }
                        	                        if (range.getMax() != null) {
                        	                            predicates.add(criteriaBuilder.lessThanOrEqualTo(numericValue, range.getMax()));
                        	                        }
                                            }
                }
            }
            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }


    /**
     * Вспомогательный класс для диапазона цен
     */
    public static class PriceRange {
        private BigDecimal min;
        private BigDecimal max;

        public PriceRange() {}

        public PriceRange(BigDecimal min, BigDecimal max) {
            this.min = min;
            this.max = max;
        }

        public BigDecimal getMin() { return min; }
        public void setMin(BigDecimal min) { this.min = min; }
        public BigDecimal getMax() { return max; }
        public void setMax(BigDecimal max) { this.max = max; }
    }
}