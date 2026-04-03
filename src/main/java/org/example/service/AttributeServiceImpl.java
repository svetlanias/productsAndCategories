package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.AttributeRepository;
import org.example.domain.Attribute;
import org.example.dto.AttributeDTO;
import org.example.dto.AttributeRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;

    private static final List<String> VALID_TYPES =
            List.of("string", "number", "boolean", "enum");

    @Override
    @Transactional
    public AttributeDTO createAttribute(AttributeRequest request) {
        validateRequest(request);

        Attribute attribute = Attribute.builder()
                .name(request.getName())
                .shortName(request.getShortName())
                .aType(request.getAType())
                .stringValues(resolveStringValues(request))
                .build();

        Attribute saved = attributeRepository.save(attribute);
        log.info("Создан атрибут с id={}, name={}", saved.getId(), saved.getName());

        return toDTO(saved);
    }

    @Override
    public List<AttributeDTO> getAllAttributes() {
        return attributeRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AttributeDTO getAttributeById(Long id) {
        return toDTO(findById(id));
    }

    @Override
    @Transactional
    public AttributeDTO updateAttribute(Long id, AttributeRequest request) {
        validateRequest(request);

        Attribute attribute = findById(id);
        attribute.setName(request.getName());
        attribute.setShortName(request.getShortName());
        attribute.setAType(request.getAType());
        attribute.setStringValues(resolveStringValues(request));

        Attribute updated = attributeRepository.save(attribute);
        log.info("Обновлён атрибут id={}", id);

        return toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteAttribute(Long id) {
        Attribute attribute = findById(id);
        attributeRepository.delete(attribute);
        log.info("Удалён атрибут id={}", id);
    }

    // ==================== Вспомогательные методы ====================

    private Attribute findById(Long id) {
        return attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Атрибут не найден: " + id));
    }

    private void validateRequest(AttributeRequest request) {
        log.debug("Валидация запроса: {}", request);

        // Проверка name
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Поле name обязательно и не должно быть пустым");
        }

        // Проверка shortName
        if (request.getShortName() == null || request.getShortName().isBlank()) {
            throw new IllegalArgumentException("Поле shortName обязательно и не должно быть пустым");
        }

        // Проверка aType
        if (request.getAType() == null || request.getAType().isBlank()) {
            throw new IllegalArgumentException("Поле aType обязательно");
        }

        // Нормализуем тип (приводим к нижнему регистру)
        String normalizedType = request.getAType().toLowerCase().trim();

        if (!VALID_TYPES.contains(normalizedType)) {
            throw new IllegalArgumentException(
                    "Недопустимый тип: '" + request.getAType() + "'. " +
                            "Допустимые значения: " + VALID_TYPES
            );
        }

        // Проверка stringValues для типа enum
        if ("enum".equals(normalizedType)) {
            if (request.getStringValues() == null || request.getStringValues().isEmpty()) {
                throw new IllegalArgumentException(
                        "Для типа 'enum' необходимо передать непустой массив stringValues"
                );
            }
        }

        log.debug("Валидация пройдена успешно");
    }

    private List<String> resolveStringValues(AttributeRequest request) {
        String normalizedType = request.getAType().toLowerCase().trim();

        if ("enum".equals(normalizedType)) {
            return request.getStringValues();
        }
        return null;
    }

    private AttributeDTO toDTO(Attribute attr) {
        return AttributeDTO.builder()
                .id(attr.getId())
                .name(attr.getName())
                .shortName(attr.getShortName())
                .aType(attr.getAType())
                .stringValues(attr.getStringValues())
                .build();
    }
}


