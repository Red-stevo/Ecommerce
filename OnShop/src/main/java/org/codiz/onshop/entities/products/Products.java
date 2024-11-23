package org.codiz.onshop.entities.products;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.codiz.onshop.repositories.products.ProductRatingsRepository;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data

public class Products implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;
    private String productName;
    private String productDescription;
    private float productPrice;
    private float discount;
    private String size;
    private String color;
    private int count;

    @OneToMany(mappedBy = "products",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductImages> productImagesList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinTable(
            name = "ProductCategories", // Name of the join table
            joinColumns = @JoinColumn(name = "product_id"), // Column for Products
            inverseJoinColumns = @JoinColumn(name = "category_id") // Column for Categories
    )
    private List<Categories> categoriesList;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<ProductRatings> productRatingsList;




}
