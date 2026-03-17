package org.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category_attributes")
@IdClass(CategoryAttribute.CategoryAttributeId.class)
public class CategoryAttribute {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cat_attr_category"))
    private Category category;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cat_attr_attribute"))
    private Attribute attribute;

    private boolean required = false;

    @Data
    public static class CategoryAttributeId implements java.io.Serializable {
        private Long category;
        private Long attribute;
    }
}
