package org.codiz.onshop.dtos.response;

import lombok.Data;

@Data
public class WishListResponse {
    private String productName;
    private String specificProductId;
    private String imageUrl;
    private float price;
    private String productColor;
    private  boolean isInStock;
}
