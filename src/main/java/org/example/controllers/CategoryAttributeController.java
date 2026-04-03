package org.example.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.CategoryAttributeDTO;
import org.example.dto.CategoryAttributeRequest;
import org.example.service.CategoryAttributeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryAttributeController {

    private final CategoryAttributeService categoryAttributeService;

    /**
     * GET /api/v1/categories/{categoryId}/attributes
     */
    @GetMapping("/{categoryId}/attributes")
    public ResponseEntity<List<CategoryAttributeDTO>> getAttributesByCategory(
            @PathVariable Long categoryId) {

        log.info("GET: получение атрибутов категории {}", categoryId);

        try {
            List<CategoryAttributeDTO> attributes =
                    categoryAttributeService.getAttributesByCategoryId(categoryId);
            return ResponseEntity.ok(attributes);
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка валидации: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Ошибка при получении атрибутов категории {}", categoryId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/v1/categories/{categoryId}/attributes
     */
    @PostMapping("/{categoryId}/attributes")
    public ResponseEntity<Void> addAttributeToCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryAttributeRequest request) {

        log.info("POST: добавление атрибута {} к категории {}",
                request.getAttributeId(), categoryId);

        try {
            categoryAttributeService.addAttributeToCategory(
                    categoryId,
                    request.getAttributeId(),
                    request.isRequired()
            );
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка валидации: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Ошибка при добавлении атрибута", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /api/v1/categories/{categoryId}/attributes/{attributeId}
     */
    @DeleteMapping("/{categoryId}/attributes/{attributeId}")
    public ResponseEntity<Void> removeAttributeFromCategory(
            @PathVariable Long categoryId,
            @PathVariable Long attributeId) {

        log.info("DELETE: удаление атрибута {} из категории {}",
                attributeId, categoryId);

        try {
            categoryAttributeService.removeAttributeFromCategory(categoryId, attributeId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка валидации: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Ошибка при удалении атрибута", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PATCH /api/v1/categories/{categoryId}/attributes/{attributeId}
     */
    @PatchMapping("/{categoryId}/attributes/{attributeId}")
    public ResponseEntity<Void> updateAttributeRequired(
            @PathVariable Long categoryId,
            @PathVariable Long attributeId,
            @RequestBody CategoryAttributeRequest request) {

        log.info("PATCH: обновление флага required для атрибута {} в категории {}",
                attributeId, categoryId);

        try {
            categoryAttributeService.updateAttributeRequired(
                    categoryId,
                    attributeId,
                    request.isRequired()
            );
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка валидации: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Ошибка при обновлении флага required", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/v1/categories/{categoryId}/attributes/{attributeId}
     */
    @GetMapping("/{categoryId}/attributes/{attributeId}")
    public ResponseEntity<CategoryAttributeDTO> getAttributeInfo(
            @PathVariable Long categoryId,
            @PathVariable Long attributeId) {

        log.info("GET: получение информации об атрибуте {} категории {}",
                attributeId, categoryId);

        try {
            List<CategoryAttributeDTO> attributes =
                    categoryAttributeService.getAttributesByCategoryId(categoryId);

            var attribute = attributes.stream()
                    .filter(a -> a.getAttributeId().equals(attributeId))
                    .findFirst();

            if (attribute.isPresent()) {
                return ResponseEntity.ok(attribute.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка валидации: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Ошибка при получении информации об атрибуте", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
