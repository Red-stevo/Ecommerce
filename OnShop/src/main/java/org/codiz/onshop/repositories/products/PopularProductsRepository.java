package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.PopularProducts;
import org.codiz.onshop.entities.products.SpecificProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PopularProductsRepository extends JpaRepository<PopularProducts,String> {


    boolean existsPopularProductsBySpecificProductDetails(SpecificProductDetails details);

    Optional<PopularProducts> findBySpecificProductDetails(SpecificProductDetails details);
}
