package org.codiz.onshop.service.serv.orders;


import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.requests.ShipmentRequest;
import org.codiz.onshop.dtos.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface OrdersService {
    EntityResponse placeOrder(OrderPlacementRequest request);
    String removeOrderItems(String orderItemId);
    EntityDeletionResponse cancelOrder(String orderId,String username);
    OrdersResponse getOrders(String orderId);
    Page<AllOrdersResponse> getAllOrdersGroupedByDate(Pageable pageable);
    EntityResponse createShipment(ShipmentRequest request);
    EntityResponse updateShipment(String trackingId, ShipmentRequest request);
    ShipmentResponse getShipment(String trackingId);
    List<ShipmentResponse> getAllShipments();
    String deleteShipment(String trackingId);
    List<ShipmentResponse> findProcessingShipment();
    List<ShipmentResponse> findDeliveredShipment();
    List<ShipmentResponse> findReturnedShipment();
    List<ShipmentResponse> findTransitShipment();
    List<AllOrdersResponse> getAllOrdersForOneWeek();
    Page<AllOrdersResponse> getDeliveredOrders(Pageable pageable);
    Page<AllOrdersResponse> getUndeliveredOrders(Pageable pageable);
    Page<AllOrdersResponse> getShippingOrders(Pageable pageable);
    Page<AllOrdersResponse> getCancelledOrders(Pageable pageable);

}
