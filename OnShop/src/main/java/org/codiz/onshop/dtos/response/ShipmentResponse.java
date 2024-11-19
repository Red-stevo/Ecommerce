package org.codiz.onshop.dtos.response;

import lombok.Data;
import org.codiz.onshop.entities.orders.ShippingStatus;

import java.time.Instant;

@Data
public class ShipmentResponse {

    private String trackingId;
    private Instant shipDate;
    private Instant deliveryDate;
    private ShippingStatus shippingStatus;

}
