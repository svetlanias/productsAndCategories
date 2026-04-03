package org.example.service;

import org.example.dto.AttributeDTO;
import org.example.dto.AttributeRequest;

import java.util.List;

public interface AttributeService {

    AttributeDTO createAttribute(AttributeRequest request);

    List<AttributeDTO> getAllAttributes();

    AttributeDTO getAttributeById(Long id);

    AttributeDTO updateAttribute(Long id, AttributeRequest request);

    void deleteAttribute(Long id);
}


