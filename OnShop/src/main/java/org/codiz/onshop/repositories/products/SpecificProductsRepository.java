package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.SpecificProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificProductsRepository extends JpaRepository<SpecificProductDetails,String> {
    SpecificProductDetails findBySpecificProductId(String specificationId);
}
