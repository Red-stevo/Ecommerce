package org.codiz.onshop.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PopularProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String popularProductId;
    @OneToOne
    @JsonIgnore
    private SpecificProductDetails specificProductDetails;
    private long count;
}
