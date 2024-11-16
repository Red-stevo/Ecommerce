package org.codiz.onshop.entities.products;

import jakarta.persistence.*;
import lombok.Data;
import org.codiz.onshop.repositories.products.ProductRatingsRepository;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data

public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;
    private String productName;
    @Column(length = 500)
    private String productDescription;
    private float productPrice;
    private String color;
    private String brand;
    private Integer quantity;
    private double discount;
    @Lob
    private String aboutProduct;



    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_image",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<ProductImages> productImages = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Categories> categories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductRatings> ratings = new ArrayList<>();




}
