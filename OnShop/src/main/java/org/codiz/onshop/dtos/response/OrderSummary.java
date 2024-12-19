package org.codiz.onshop.dtos.response;

import lombok.Data;

@Data
public class OrderSummary {
    private String orderDate;
    private String orderTime;
    private float orderTotal;
    private float deliveryFee;
    private float totalCharges;
}
