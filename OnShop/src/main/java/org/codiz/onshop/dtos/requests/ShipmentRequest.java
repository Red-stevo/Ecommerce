package org.codiz.onshop.dtos.requests;

import lombok.Data;
import org.codiz.onshop.entities.orders.Orders;
import org.codiz.onshop.entities.orders.ShippingStatus;

import java.time.Instant;

@Data
public class ShipmentRequest {
    private String orderId;
    private Instant shipDate;
    private Instant deliveryDate;
    private ShippingStatus status;

}
