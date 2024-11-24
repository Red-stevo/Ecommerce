package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.ProductRatings;
import org.codiz.onshop.entities.products.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRatingsRepository extends JpaRepository<ProductRatings, String> {

        /*@Query("SELECT AVG(r.rating) FROM ProductRatings r WHERE r.product = :productId")
        Double findAverageRatingByProductId(@Param("productId") String productId);*/

        List<ProductRatings> findAllProductRatingsByProduct(Products products);

        @Query("SELECT AVG(r.rating) FROM ProductRatings r WHERE r.product.productId = :productId")
        Double findAverageRatingByProductId(@Param("productId") String productId);

}
