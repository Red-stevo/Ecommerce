package org.codiz.onshop.dtos.requests;


import lombok.Data;
import org.codiz.onshop.entities.orders.ShippingStatus;

@Data
public class ShippingStatusUpdateModel {

    ShippingStatus status;
}
