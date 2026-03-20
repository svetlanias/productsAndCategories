package org.example.controllers;

import org.example.dto.CategoryAttributeDTO;
import org.example.service.AttributeService;
import org.example.service.CategoryAttributeService;
import org.example.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryAttributeController {

    private final CategoryAttributeService categoryAttributeService;
    private final AttributeService attributeService;
    private final CategoryService categoryService;

    public CategoryAttributeController(
            CategoryAttributeService categoryAttributeService,
            AttributeService attributeService,
            CategoryService categoryService) {
        this.categoryAttributeService = categoryAttributeService;
        this.attributeService = attributeService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{categoryId}/attributes")
    public String manageAttributes(@PathVariable Long categoryId, Model model) {
        // Текущие привязанные атрибуты
        List<CategoryAttributeDTO> boundAttributes = categoryAttributeService.getAttributesByCategoryId(categoryId);
        // Все доступные атрибуты (для выпадающего списка)
        var allAttributes = attributeService.getAllAttributes();

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("boundAttributes", boundAttributes);
        model.addAttribute("allAttributes", allAttributes);
        model.addAttribute("newBinding", new CategoryAttributeDTO());
        return "category-attributes";
    }

    @PostMapping("/{categoryId}/attributes")
    public String addAttributeToCategory(
            @PathVariable Long categoryId,
            @RequestParam Long attributeId,
            @RequestParam(defaultValue = "false") boolean required) {
        categoryAttributeService.addAttributeToCategory(categoryId, attributeId, required);
        return "redirect:/categories/" + categoryId + "/attributes";
    }
}
