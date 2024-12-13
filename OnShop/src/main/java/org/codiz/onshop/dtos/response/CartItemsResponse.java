package org.codiz.onshop.dtos.response;

import lombok.Data;

@Data
public class CartItemsResponse {
    private String cartItemId;
    private String productImageUrl;

    private String productName;

    private String productId;
    private boolean inStock;

    private float productPrice;
    private int count;
    private String color;
}
