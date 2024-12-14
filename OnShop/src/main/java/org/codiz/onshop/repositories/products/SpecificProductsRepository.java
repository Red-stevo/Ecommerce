package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.SpecificProductDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecificProductsRepository extends JpaRepository<SpecificProductDetails,String> {
    Optional<SpecificProductDetails> findBySpecificProductId(String specificationId);

    Page<SpecificProductDetails> findAllByProductPriceBetween(float productPrice, float productPrice2, Pageable pageable);

    Page<SpecificProductDetails> findAllByColorContainingIgnoreCaseOrSizeContainingIgnoreCase(String query, String query1, Pageable pageable);


}
