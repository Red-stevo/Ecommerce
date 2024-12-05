package org.codiz.onshop.repositories.products;

import org.codiz.onshop.entities.products.Inventory;
import org.codiz.onshop.entities.products.InventoryStatus;
import org.codiz.onshop.entities.products.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {
    Inventory findByProducts(Products products);

    Page<Inventory> findAllByStatus(InventoryStatus inventoryStatus, Pageable pageable);
}
