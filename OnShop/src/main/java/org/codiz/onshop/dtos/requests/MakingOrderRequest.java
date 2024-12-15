package org.codiz.onshop.dtos.requests;

import lombok.Data;

@Data
public class MakingOrderRequest {
    private String specificationId;
    private int quantity;
}