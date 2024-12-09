package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.SpecificProductDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificProductsRepository extends JpaRepository<SpecificProductDetails,String> {
    SpecificProductDetails findBySpecificProductId(String specificationId);

    Page<SpecificProductDetails> findAllByProductPriceBetween(float productPrice, float productPrice2, Pageable pageable);

    Page<SpecificProductDetails> findAllByColorContainingIgnoreCaseOrSizeContainingIgnoreCase(String query, String query1, Pageable pageable);
}
