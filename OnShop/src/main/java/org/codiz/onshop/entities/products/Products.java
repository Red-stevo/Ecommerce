package org.codiz.onshop.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(length = 1000)
    private String productDescription;


    @OneToMany(mappedBy = "products",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<SpecificProductDetails>specificProductDetailsList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@JsonManagedReference
    @JoinTable(
            name = "ProductCategories", // Name of the join table
            joinColumns = @JoinColumn(name = "product_id"), // Column for Products
            inverseJoinColumns = @JoinColumn(name = "category_id") // Column for Categories
    )
    @JsonIgnore
    @ToString.Exclude
    private List<Categories> categoriesList;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<ProductRatings> productRatingsList;

    @ManyToOne(fetch = FetchType.EAGER,optional = true,cascade = CascadeType.ALL)
    @JoinColumn(name = "inventoryId",nullable = false)
    private Inventory inventory;




}
