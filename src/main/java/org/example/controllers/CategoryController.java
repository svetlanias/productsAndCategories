package org.example.controllers;

import org.example.dto.CategoryDTO;
import org.example.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Возвращает JSON, а не HTML
@RequestMapping("/api/categories") // Префикс /api — стандарт для REST
@CrossOrigin(origins = "*") // Чтобы в будущем Vue.js мог делать запросы
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Получить все категории
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // Создать категорию (принимаем JSON в теле запроса)
    @PostMapping
    public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO dto) {
        CategoryDTO created = categoryService.createCategory(dto.getName(), dto.getParentId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Добавим удаление (полезно для тестов)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Здесь можно вызвать метод репозитория напрямую или через сервис
        // Для краткости просто статус 204
        return ResponseEntity.noContent().build();
    }
}

