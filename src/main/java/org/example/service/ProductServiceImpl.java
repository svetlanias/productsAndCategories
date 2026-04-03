package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dao.CategoryRepository;
import org.example.dao.ProductRepository;
import org.example.domain.Category;
import org.example.domain.Product;
import org.example.dto.ProductDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final String uploadPath = "src/main/resources/static/images/products";

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO dto, MultipartFile image) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        String imagePath = saveImage(image);

        Product product = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .maker(dto.getMaker())
                .category(category)
                .attributes(dto.getAttributes())
                .imagePath(imagePath)
                .active(true)
                .build();

        return convertToDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO dto, MultipartFile image) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setMaker(dto.getMaker());
        product.setAttributes(dto.getAttributes());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Категория не найдена"));
            product.setCategory(category);
        }

        if (image != null && !image.isEmpty()) {
            deleteImage(product.getImagePath()); // Удаляем старое фото
            product.setImagePath(saveImage(image));
        }

        return convertToDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        deleteImage(product.getImagePath());
        productRepository.delete(product);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Вспомогательные методы
    private String saveImage(MultipartFile image) {
        if (image == null || image.isEmpty()) return null;
        try {
            String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path path = Paths.get(uploadPath);
            if (!Files.exists(path)) Files.createDirectories(path);
            Files.write(path.resolve(filename), image.getBytes());
            return "/images/products/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении изображения", e);
        }
    }

    private void deleteImage(String imagePath) {
        if (imagePath == null) return;
        try {
            Path path = Paths.get("src/main/resources/static" + imagePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .maker(product.getMaker())
                .categoryId(product.getCategory().getId())
                .imagePath(product.getImagePath())
                .attributes(product.getAttributes())
                .build();
    }
}

