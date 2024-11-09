package org.codiz.onshop.entities.cart;

import jakarta.persistence.*;
import lombok.Data;
import org.codiz.onshop.entities.products.Products;

@Entity
@Data
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cartItemId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Cart cart;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Products products;
    private Integer quantity;

    public void setQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive integer");
        }
        this.quantity = quantity;
    }
}
