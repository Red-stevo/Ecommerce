package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.ProductImages;
import org.codiz.onshop.entities.products.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, String> {

    Optional<ProductImages> findByImageUrl(String image);
}
