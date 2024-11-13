package org.codiz.onshop.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class ProductCreationRequest {

    private String productName;

    private String productDescription;

    private float productPrice;

    private List<String> productUrls;
    //private List<MultipartFile> productUrls;

    private Integer quantity;

}
