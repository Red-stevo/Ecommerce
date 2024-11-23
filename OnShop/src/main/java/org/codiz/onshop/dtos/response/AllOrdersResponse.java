package org.codiz.onshop.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class AllOrdersResponse {
    private String orderId;
    private String orderStatus;
    private String orderDate;
    private float orderTotal;
    private String orderAddress;
    private List<OrderItemsResponse> items;
}
