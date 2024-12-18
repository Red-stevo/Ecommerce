package org.codiz.onshop.dtos.response;

import lombok.Data;


import java.time.Instant;

@Data
public class InventoryResponse {
    private String productName;
    private float unitPrice;
    private int quantity;
    private int status;
    private String imageUrl;
    private String productId;
}
