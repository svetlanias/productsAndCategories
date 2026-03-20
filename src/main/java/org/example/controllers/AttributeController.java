package org.example.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.AttributeDTO;
import org.example.service.AttributeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/attributes")
public class AttributeController {

    private final AttributeService attributeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @GetMapping
    public String list(Model model) {
        List<AttributeDTO> attributes = attributeService.getAllAttributes();
        model.addAttribute("attributes", attributes);
        model.addAttribute("newAttribute", new AttributeDTO());
        return "attributes";
    }

    @PostMapping
    public String create(@RequestParam String name,
                         @RequestParam String shortName,
                         @RequestParam String aType,
                         @RequestParam(required = false) String stringValuesJson) {

        List<String> stringValues = null;
        if ("enum".equals(aType) && stringValuesJson != null && !stringValuesJson.trim().isEmpty()) {
            try {
                stringValues = objectMapper.readValue(stringValuesJson, new TypeReference<List<String>>() {});
            } catch (Exception e) {
                throw new RuntimeException("Неверный формат JSON для значений: " + stringValuesJson);
            }
        }

        attributeService.createAttribute(name, shortName, aType, stringValues);
        return "redirect:/attributes";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        AttributeDTO attribute = attributeService.getAttributeById(id);
        model.addAttribute("attribute", attribute);
        return "attribute-edit";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long id,
                         @RequestParam String name,
                         @RequestParam String shortName,
                         @RequestParam String aType,
                         @RequestParam(required = false) String stringValuesJson) {

        List<String> stringValues = null;
        if ("enum".equals(aType) && stringValuesJson != null && !stringValuesJson.trim().isEmpty()) {
            try {
                stringValues = objectMapper.readValue(stringValuesJson, new TypeReference<List<String>>() {});
            } catch (Exception e) {
                throw new RuntimeException("Неверный формат JSON: " + stringValuesJson);
            }
        }

        attributeService.updateAttribute(id, name, shortName, aType, stringValues);
        return "redirect:/attributes";
    }
}