package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Categories, String> {
    Page<Categories> findCategoriesByCategoryNameIgnoreCase(String categoryName, Pageable pageable);
    Optional<Categories> findCategoriesByCategoryNameIgnoreCase(String categoryName);
}
