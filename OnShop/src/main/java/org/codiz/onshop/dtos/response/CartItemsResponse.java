package org.codiz.onshop.dtos.response;

import lombok.Data;

@Data
public class CartItemsResponse {
    private String cartItemId;
    private String productImageUrl;
    private String productName;
    private String productId;
    private float productPrice;

}