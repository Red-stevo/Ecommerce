package org.codiz.onshop.entities.products;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Data
@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String inventoryId;
    private int inStockQuantity;
    private int quantitySold;
    private Instant lastUpdate;


    @OneToOne
    @JoinColumn(name = ("product_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Products products;
}
