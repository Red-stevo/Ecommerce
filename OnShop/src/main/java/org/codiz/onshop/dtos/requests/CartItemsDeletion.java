package org.codiz.onshop.dtos.requests;

import lombok.Data;

@Data
public class CartItemsDeletion {
    private String cartId;
    private String cartItemId;
}
