package org.codiz.onshop.repositories.products;

import jakarta.transaction.Transactional;
import org.codiz.onshop.entities.products.ProductImages;
import org.codiz.onshop.entities.products.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, String> {

    Optional<ProductImages> findByImageUrl(String image);
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductImages p WHERE p.imageUrl = :imageUrl")
    void deleteByImageUrl(@Param("imageUrl") String imageUrl);
}
