package org.codiz.onshop.dtos.requests;

import lombok.Data;
import org.codiz.onshop.entities.cart.CartItems;
import org.codiz.onshop.entities.cart.Status;

import java.util.List;

@Data
public class CartItemsUpdate {
    private String cartId;
    private String cartItemId;
    private int quantity;
}
