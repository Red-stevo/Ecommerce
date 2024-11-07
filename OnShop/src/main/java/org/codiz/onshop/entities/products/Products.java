package org.codiz.onshop.entities.products;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;
    private String productName;
    private String productDescription;
    private float productPrice;
    private Integer quantity;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_image",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<ProductImages> productImages;

}
