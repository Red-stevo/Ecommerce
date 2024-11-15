package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Categories, String> {
    Optional<Categories> findCategoriesByCategoryNameIgnoreCase(String categoryName);
}
