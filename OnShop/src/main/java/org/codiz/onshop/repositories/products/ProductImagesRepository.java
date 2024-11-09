package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, String> {
}
