package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.Categories;
import org.codiz.onshop.entities.products.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsJpaRepository extends JpaRepository<Products, String>  {
    Products findByProductId(String productId);

    Page<Products> findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(String query, String query1, Pageable pageable);

    Optional<Products> findProductsByProductId(String productId);

    List<Products> findAllByProductNameContainingIgnoreCaseOrCategoriesListContainingIgnoreCase(String productName, Categories categoriesList);

    List<Products> findAllProductsByCategoriesList(List<Categories> categoriesList);

    List<Products> findAllByProductNameAndProductDescriptionLike(String productName, String productDescription);

    Page<Products> findAllByProductNameContainsIgnoreCaseOrProductDescriptionContainingIgnoreCaseOrCategoriesList(String productName,String productDescription,List<Categories> categories, Pageable pageable);

    Page<Products> findAllByCategoriesList_CategoryIdIn(List<String> categoryIds, Pageable pageable);
}
