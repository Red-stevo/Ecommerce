package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.ProductRatings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRatingsRepository extends JpaRepository<ProductRatings, String> {
}
