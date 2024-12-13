package org.codiz.onshop.dtos.response;

import lombok.Data;
import org.codiz.onshop.entities.orders.OrderItems;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Data
public class OrdersResponse {

    private String orderNumber;

    private String orderStatus;

    private OrderSummary orderSummary;

    private List<OrderItemsResponse> itemsList;

    private CustomerDetails customerDetails;

    private double totalCharges;

    private String address;

}
