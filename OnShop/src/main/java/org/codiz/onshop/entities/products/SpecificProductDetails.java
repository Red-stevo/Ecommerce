package org.codiz.onshop.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
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

    @OneToMany(mappedBy = "specificProductDetails",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<ProductImages> productImagesList;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    private Products products;
}
