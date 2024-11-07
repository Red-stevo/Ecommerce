package org.codiz.onshop.entities.products;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ProductImages {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String imageId;
    private String imageUrl;
}
