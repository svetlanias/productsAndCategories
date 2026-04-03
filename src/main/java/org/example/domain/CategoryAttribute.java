package org.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "category_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryAttribute {

    @EmbeddedId
    private CategoryAttributeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("attributeId")
    @JoinColumn(name = "attribute_id", nullable = false)
    private Attribute attribute;

    @Column(nullable = false)
    private boolean required = false;

    /**
     * Составной ключ для CategoryAttribute
     */
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryAttributeId implements Serializable {

        @Column(name = "category_id")
        private Long categoryId;

        @Column(name = "attribute_id")
        private Long attributeId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CategoryAttributeId that = (CategoryAttributeId) o;
            return Objects.equals(categoryId, that.categoryId) &&
                    Objects.equals(attributeId, that.attributeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(categoryId, attributeId);
        }
    }
}

