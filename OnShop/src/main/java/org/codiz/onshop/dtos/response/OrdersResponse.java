package org.codiz.onshop.dtos.response;

import lombok.Data;
import org.codiz.onshop.entities.orders.OrderItems;

import java.sql.Timestamp;
import java.util.List;

@Data
public class OrdersResponse {

    private Timestamp orderDate;
    private double totalPrice;
    private String address;
    private List<OrderItemsResponse> itemsList;
}
