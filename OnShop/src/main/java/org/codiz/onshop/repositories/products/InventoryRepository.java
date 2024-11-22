package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.Inventory;
import org.codiz.onshop.entities.products.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByProducts(Products products);
}
