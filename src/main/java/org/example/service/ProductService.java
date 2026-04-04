package org.example.service;

import org.example.dto.FacetResponse;
import org.example.dto.ProductDTO;
import org.example.dto.ProductFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO, MultipartFile image);
    ProductDTO updateProduct(Long id, ProductDTO productDTO, MultipartFile image);
    void deleteProduct(Long id);
    ProductDTO getProductById(Long id);
    List<ProductDTO> getAllProducts();
    List<ProductDTO> getProductsByCategory(Long categoryId);

    // Фасеточная фильтрация
    Page<ProductDTO> filterProducts(ProductFilterRequest filter);
    FacetResponse getFacets(ProductFilterRequest filter);
}
