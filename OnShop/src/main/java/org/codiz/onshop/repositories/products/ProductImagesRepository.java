package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.ProductImages;
import org.codiz.onshop.entities.products.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, String> {
    List<ProductImages> findByProducts_ProductId(String productId);
    List<ProductImages> findByProducts(Products products);
    ;
}
