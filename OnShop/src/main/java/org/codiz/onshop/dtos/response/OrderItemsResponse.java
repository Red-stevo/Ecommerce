package org.codiz.onshop.dtos.response;

import lombok.Data;
import org.codiz.onshop.entities.orders.OrderItemStatus;
import org.codiz.onshop.entities.products.Products;

@Data
public class OrderItemsResponse {
    private String productId;
    private String productName;
    private String productImageUrl;
    private float productPrice;
    private int quantity;
    private double totalPrice;
    private OrderItemStatus status;
    private String stockStatus;
    private boolean cancelled;
}
