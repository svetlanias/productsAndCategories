package org.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ProductDTO;
import org.example.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Получить все товары
    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.getAllProducts();
    }

    // Получить один товар
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDTO> create(@ModelAttribute ProductDTO productDTO,
                                             @RequestParam(value = "image", required = false) MultipartFile image) {
        return new ResponseEntity<>(productService.createProduct(productDTO, image), HttpStatus.CREATED);
    }

    // Обновить товар
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDTO> update(@PathVariable Long id,
                                             @ModelAttribute ProductDTO productDTO,
                                             @RequestParam(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO, image));
    }

    // Удалить товар
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Товары по категории
    @GetMapping("/category/{categoryId}")
    public List<ProductDTO> getByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }
}

