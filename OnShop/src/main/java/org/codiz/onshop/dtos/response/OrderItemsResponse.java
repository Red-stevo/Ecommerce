package org.codiz.onshop.dtos.response;

import lombok.Data;
import org.codiz.onshop.entities.products.Products;

@Data
public class OrderItemsResponse {
    private Products productId;
    private int quantity;
    private double totalPrice;
}
