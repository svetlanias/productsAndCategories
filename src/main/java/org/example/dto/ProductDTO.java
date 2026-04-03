package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;              // Добавлено для ответа
    private String name;
    private BigDecimal price;
    private String description;
    private String maker;
    private Long categoryId;
    private String imagePath;     // Добавлено для ответа
    private Map<String, Object> attributes = new HashMap<>();
}


