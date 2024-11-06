package org.codiz.onshop.dtos.requests;

import lombok.Data;

@Data
public class ProductCreationRequest {

    private String productName;
    private String productDescription;
    private float productPrice;
    private ProductUrls productUrls;
    private Integer quantity;
}
