package org.codiz.onshop.dtos.response;


import lombok.Data;

@Data
public class DiscountedProductsResponse {
    private String productId;
    private String productName;
    private int ratings;
    private String productImagesUrl;
    private double discountedPrice;
    private float discount;
}