package org.codiz.onshop.dtos.requests;

import lombok.Data;
import org.codiz.onshop.entities.products.Products;

@Data
public class OrderItemsRequests {
    private String productId;
    private int quantity;
}
