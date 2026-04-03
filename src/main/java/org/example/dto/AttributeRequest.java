package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeRequest {

    private String name;
    private String shortName;

    @JsonProperty("aType")
    private String aType;

    // Передаётся только если aType = "enum"
    private List<String> stringValues;
}

