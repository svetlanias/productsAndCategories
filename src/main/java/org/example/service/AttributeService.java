package org.example.service;

import org.example.dto.AttributeDTO;

import java.util.List;

public interface AttributeService {
    AttributeDTO createAttribute(String name, String shortName, String aType, List<String> stringValues);
    List<AttributeDTO> getAllAttributes();
    AttributeDTO getAttributeById(Long id);
    void updateAttribute(Long id, String name, String shortName, String aType, List<String> stringValues);
}
