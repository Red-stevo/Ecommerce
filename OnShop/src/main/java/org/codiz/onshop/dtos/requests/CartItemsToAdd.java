package org.codiz.onshop.dtos.requests;

import lombok.Data;

@Data
public class CartItemsToAdd {


    private String specificProductId;

    private int quantity;

}
