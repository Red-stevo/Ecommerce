package org.codiz.onshop.entities.orders;

import lombok.Data;


public enum OrderStatus {
    DELIVERED,
    UNDELIVERED,
    SHIPPING,
    CANCELLED,
}
