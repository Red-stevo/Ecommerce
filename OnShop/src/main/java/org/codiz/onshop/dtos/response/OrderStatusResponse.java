package org.codiz.onshop.dtos.response;

import lombok.Data;
import org.codiz.onshop.entities.orders.ShippingStatus;

import java.time.Instant;
import java.util.List;

@Data
public class OrderStatusResponse {

    private String orderId;
    private String status;
    private List<OrderTrackingProducts> products;

}
