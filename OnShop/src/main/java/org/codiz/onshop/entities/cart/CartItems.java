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
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Products products;
    private Integer quantity;
}
