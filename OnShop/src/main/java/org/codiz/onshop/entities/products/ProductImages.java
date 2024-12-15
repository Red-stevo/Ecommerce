package org.codiz.onshop.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProductImages {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String imageId;
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private SpecificProductDetails specificProductDetails;
}
