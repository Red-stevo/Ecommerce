package org.codiz.onshop.dtos.requests;

import lombok.Data;

@Data
public class MakingOrderRequest {
    private String specificProductId;
    private int quantity;
}