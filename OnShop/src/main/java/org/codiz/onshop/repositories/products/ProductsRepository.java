package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products, String> {
}
