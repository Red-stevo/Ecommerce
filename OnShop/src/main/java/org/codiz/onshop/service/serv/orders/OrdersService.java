package org.codiz.onshop.service.serv.orders;


import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.requests.ShipmentRequest;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.orders.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface OrdersService {
    EntityResponse placeOrder(OrderPlacementRequest request);
    String removeOrderItems(String orderItemId,String userId);
    EntityDeletionResponse cancelOrder(String orderId,String username);
    OrdersResponse getOrders(String orderId);
    Page<AllOrdersResponse> getAllOrdersGroupedByDate(Pageable pageable);
    List<AllOrdersResponse> getAllOrdersForOneWeek();
    Page<AllOrdersResponse> getDeliveredOrders(Pageable pageable);
    Page<AllOrdersResponse> getUndeliveredOrders(Pageable pageable);
    Page<AllOrdersResponse> getShippingOrders(Pageable pageable);
    Page<AllOrdersResponse> getCancelledOrders(Pageable pageable);
    String updateStatus(String orderId, OrderStatus status);
    OrderStatusResponse getOrderStatus(String orderId);

}
