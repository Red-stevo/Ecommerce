package org.codiz.onshop.dtos.requests;

import lombok.Data;

@Data
public class CartItemsToAdd {

    private String userId;
    //private String productId;
    private String specificationId;
    private int quantity;
}
