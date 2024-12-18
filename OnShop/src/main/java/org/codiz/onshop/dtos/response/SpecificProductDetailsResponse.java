package org.codiz.onshop.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class SpecificProductDetailsResponse {
    private String id;
    private float productPrice;
    private float productOldPrice;
    private String productColor;
    private float discount;
    private String productSize;
    private List<String> productImages;
    private int productCount;
    private int status;
}
