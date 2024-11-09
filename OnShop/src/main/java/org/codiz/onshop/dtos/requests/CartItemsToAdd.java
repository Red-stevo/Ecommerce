package org.codiz.onshop.dtos.requests;

import lombok.Data;

@Data
public class CartItemsToAdd {

    private String productId;
    private int quantity;
    private String cartId;
}
