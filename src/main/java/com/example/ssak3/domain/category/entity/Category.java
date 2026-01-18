package com.example.ssak3.domain.category.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.domain.category.model.request.CategoryUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public Category(String name) {
        this.name = name;
    }

    public void update(CategoryUpdateRequest request) {
        if (request.getName() != null) {
            this.name = request.getName();
        }
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
