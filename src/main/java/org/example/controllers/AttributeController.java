package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.AttributeDTO;
import org.example.dto.AttributeRequest;
import org.example.service.AttributeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attributes")
@RequiredArgsConstructor
public class AttributeController {

    private final AttributeService attributeService;

    // GET /api/attributes
    @GetMapping
    public ResponseEntity<List<AttributeDTO>> getAll() {
        return ResponseEntity.ok(attributeService.getAllAttributes());
    }

    // GET /api/attributes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AttributeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(attributeService.getAttributeById(id));
    }

    // POST /api/attributes
    @PostMapping
    public ResponseEntity<AttributeDTO> create(@RequestBody AttributeRequest request) {
        AttributeDTO created = attributeService.createAttribute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/attributes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AttributeDTO> update(
            @PathVariable Long id,
            @RequestBody AttributeRequest request) {
        AttributeDTO updated = attributeService.updateAttribute(id, request);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/attributes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attributeService.deleteAttribute(id);
        return ResponseEntity.noContent().build();
    }
}
