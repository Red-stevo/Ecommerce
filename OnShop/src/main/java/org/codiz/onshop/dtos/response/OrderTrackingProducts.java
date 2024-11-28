package org.codiz.onshop.dtos.response;

import lombok.Data;

@Data
public class OrderTrackingProducts {
    private String productImageUrl;
    private String productName;
    private float productPrice;
}
