package org.codiz.onshop.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String inventoryId;

    @Enumerated(EnumType.STRING) // Ensure enum is stored as a string
    private InventoryStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "product_inventory",
            joinColumns = @JoinColumn(name = "inventoryId"),
            inverseJoinColumns = @JoinColumn(name = "productId")
    )
    @JsonIgnore
    private List<Products> products = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = InventoryStatus.ACTIVE;
        }
    }
}

