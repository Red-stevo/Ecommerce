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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;
    private int quantityBought;
    private Instant lastUpdate;
    private int quantitySold;
    private double buyPrice;

    @OneToOne
    @JoinColumn(name = ("product_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Products products;
}
