package org.codiz.onshop.service.serv.orders;


import org.codiz.onshop.dtos.requests.LocationRequest;
import org.codiz.onshop.dtos.requests.MakingOrderRequest;
import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.orders.OrderStatus;
import org.codiz.onshop.entities.orders.ShippingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrdersService {
    EntityResponse placeOrder(OrderPlacementRequest request);
   HttpStatus removeOrderItems(String orderItemId,String userId);
    EntityDeletionResponse cancelOrder(String orderId,String username);
    OrdersResponse getOrders(String orderId);
    Page<AllOrdersResponse> getAllOrdersGroupedByDate(Pageable pageable);
    List<AllOrdersResponse> getAllOrdersForOneWeek();
    Page<AllOrdersResponse> getDeliveredOrders(Pageable pageable);
    Page<AllOrdersResponse> getUndeliveredOrders(Pageable pageable);
    Page<AllOrdersResponse> getShippingOrders(Pageable pageable);
    Page<AllOrdersResponse> getCancelledOrders(Pageable pageable);
    String updateStatus(String orderId, OrderStatus status);
    OrderStatusResponse getShippingStatus(String userId);
    String updateShippingStatus(String orderId, ShippingStatus status);
    ResponseEntity addOrderItemQuantity(String orderIteId, int quantity);
    HttpStatus makeOrder(List<MakingOrderRequest> request, String userId);
    PaymentDetails getPaymentDetails(String userId);
    HttpStatus updateShippingDetails(String userId, LocationRequest locationRequest);
    HttpStatus updateShippingQuantity(String orderItemId, int quantity);
}
