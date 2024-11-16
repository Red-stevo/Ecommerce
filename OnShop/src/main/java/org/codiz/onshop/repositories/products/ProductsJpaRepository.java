package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsJpaRepository extends JpaRepository<Products, String>  {
    Products findByProductId(String productId);

    Page<Products> findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(String query, String query1, Pageable pageable);
}
