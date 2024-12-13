package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.PopularProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularProductsRepository extends JpaRepository<PopularProducts,String> {



}
