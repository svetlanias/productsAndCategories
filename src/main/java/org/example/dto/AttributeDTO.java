package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeDTO {
    private Long id;
    private String name;
    private String shortName;
    private String aType;
    private List<String> stringValues;
    private String stringValuesJson;
}
