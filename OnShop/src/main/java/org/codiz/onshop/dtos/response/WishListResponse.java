package org.codiz.onshop.dtos.response;

import lombok.Data;

@Data
public class WishListResponse {
    private String productName;
    private String imageUrl;
    private float price;
    private  boolean isInStock;
}
