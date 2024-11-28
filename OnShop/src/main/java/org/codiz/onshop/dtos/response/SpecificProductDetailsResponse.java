package org.codiz.onshop.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class SpecificProductDetailsResponse {
    private String productId;
    private float productPrice;
    private float productOldPrice;
    private String productColor;
    private String productSize;
    private List<String> productImages;
    private int productCount;
}
