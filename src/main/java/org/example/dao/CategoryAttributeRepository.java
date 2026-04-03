package org.example.dao;

import org.example.domain.CategoryAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryAttributeRepository
        extends JpaRepository<CategoryAttribute, CategoryAttribute.CategoryAttributeId> {

    // Найти все атрибуты категории
    @Query("SELECT ca FROM CategoryAttribute ca WHERE ca.category.id = :categoryId")
    List<CategoryAttribute> findByCategoryId(@Param("categoryId") Long categoryId);

    // Проверить наличие связи
    @Query("SELECT CASE WHEN COUNT(ca) > 0 THEN true ELSE false END " +
            "FROM CategoryAttribute ca " +
            "WHERE ca.category.id = :categoryId AND ca.attribute.id = :attributeId")
    boolean existsByCategoryIdAndAttributeId(
            @Param("categoryId") Long categoryId,
            @Param("attributeId") Long attributeId);

    // Найти конкретную связь
    @Query("SELECT ca FROM CategoryAttribute ca " +
            "WHERE ca.category.id = :categoryId AND ca.attribute.id = :attributeId")
    Optional<CategoryAttribute> findByCategoryIdAndAttributeId(
            @Param("categoryId") Long categoryId,
            @Param("attributeId") Long attributeId);
}


