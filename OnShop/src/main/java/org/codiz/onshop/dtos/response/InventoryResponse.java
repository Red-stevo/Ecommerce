package org.codiz.onshop.dtos.response;

import lombok.Data;
import org.codiz.onshop.entities.products.InventoryStatus;

import java.time.Instant;

@Data
public class InventoryResponse {
    private String productName;
    private float unitPrice;
    private int quantity;
    private InventoryStatus status;
    private String imageUrl;
}
