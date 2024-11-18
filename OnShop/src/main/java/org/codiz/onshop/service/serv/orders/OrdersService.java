package org.codiz.onshop.service.serv.orders;


import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.response.EntityDeletionResponse;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.dtos.response.OrdersResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface OrdersService {
    EntityResponse placeOrder(OrderPlacementRequest request);
    String removeOrderItems(String orderItemId);
    EntityDeletionResponse cancelOrder(String orderId);
    List<OrdersResponse> getOrders(String userId);
    Map<LocalDate, List<OrdersResponse>> getAllOrdersGroupedByDate();
}
