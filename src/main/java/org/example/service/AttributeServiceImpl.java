package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.dao.AttributeRepository;
import org.example.domain.Attribute;
import org.example.dto.AttributeDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AttributeServiceImpl(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    @Override
    @Transactional
    public AttributeDTO createAttribute(String name, String shortName, String aType, List<String> stringValues) {
        Attribute attribute = new Attribute();
        attribute.setName(name);
        attribute.setShortName(shortName);
        attribute.setAType(aType);
        attribute.setStringValues(stringValues);
        Attribute saved = attributeRepository.save(attribute);
        return buildDTO(saved);
    }

    @Override
    public List<AttributeDTO> getAllAttributes() {
        return attributeRepository.findAll().stream()
                .map(this::buildDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AttributeDTO getAttributeById(Long id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Атрибут не найден: " + id));
        return buildDTO(attribute);
    }

    @Override
    @Transactional
    public void updateAttribute(Long id, String name, String shortName, String aType, List<String> stringValues) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Атрибут не найден: " + id));

        attribute.setName(name);
        attribute.setShortName(shortName);
        attribute.setAType(aType);
        attribute.setStringValues(stringValues);

        attributeRepository.save(attribute);
    }

    private AttributeDTO buildDTO(Attribute attr) {
        String json = null;
        if (attr.getStringValues() != null && !attr.getStringValues().isEmpty()) {
            try {
                json = objectMapper.writeValueAsString(attr.getStringValues());
            } catch (Exception e) {
            }
        }
        return new AttributeDTO(
                attr.getId(),
                attr.getName(),
                attr.getShortName(),
                attr.getAType(),
                attr.getStringValues(),
                json
        );
    }
}
