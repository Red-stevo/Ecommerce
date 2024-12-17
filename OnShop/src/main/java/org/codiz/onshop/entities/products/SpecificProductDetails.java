package org.codiz.onshop.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.codiz.onshop.entities.cart.CartItems;
import org.codiz.onshop.entities.orders.OrderItems;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @OneToMany(mappedBy = "specificProductDetails",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProductImages> productImagesList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    private Products products;


    @ManyToOne(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
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

    @OneToMany(mappedBy = "specificProductDetails",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER,orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<OrderItems> orderItems;
}
