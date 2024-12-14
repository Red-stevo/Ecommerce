package org.codiz.onshop.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.codiz.onshop.entities.cart.CartItems;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Entity
@Data
public class SpecificProductDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String specificProductId;
    private float productPrice;
    private float discount;
    private String size;
    private String color;
    private int count;
    private Instant createdAt;

    @OneToMany(mappedBy = "specificProductDetails",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProductImages> productImagesList;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Products products;


    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WishList wishList;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "cart_items")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CartItems cartItems;
}
