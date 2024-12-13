package org.codiz.onshop.entities.cart;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.entities.products.SpecificProductDetails;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @ToString.Exclude
    @JsonIgnore
    private Cart cart;

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    //@OnDelete(action = OnDeleteAction.NO_ACTION)
    private SpecificProductDetails products;

    private Integer quantity;


    @JsonIgnore // Break the cycle
    public Cart getCart() {
        return cart;
    }


}
