package org.codiz.onshop.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class ProductImages {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String imageId;
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private Products products;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private SpecificProductDetails specificProductDetails;
}
