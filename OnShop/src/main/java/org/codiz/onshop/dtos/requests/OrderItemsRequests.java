package org.codiz.onshop.dtos.requests;

import lombok.Data;
import org.codiz.onshop.entities.products.Products;

@Data
public class OrderItemsRequests {
    private int quantity;
    private String specificationId;
}
