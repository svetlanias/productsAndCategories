package org.example.controllers;

import org.example.dto.CategoryDTO;
import org.example.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("newCategory", new CategoryDTO());
        return "categories";
    }

    @PostMapping
    public String create(@RequestParam String name,
                         @RequestParam(required = false) Long parentId) {
        categoryService.createCategory(name, parentId);
        return "redirect:/categories";
    }
}
