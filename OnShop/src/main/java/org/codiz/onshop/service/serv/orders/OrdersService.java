package org.codiz.onshop.service.serv.orders;


import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.requests.ShipmentRequest;
import org.codiz.onshop.dtos.response.EntityDeletionResponse;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.dtos.response.OrdersResponse;
import org.codiz.onshop.dtos.response.ShipmentResponse;
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
    EntityResponse createShipment(ShipmentRequest request);
    EntityResponse updateShipment(String trackingId, ShipmentRequest request);
    ShipmentResponse getShipment(String trackingId);
    List<ShipmentResponse> getAllShipments();
    String deleteShipment(String trackingId);
    List<ShipmentResponse> findProcessingShipment();
    List<ShipmentResponse> findDeliveredShipment();
    List<ShipmentResponse> findReturnedShipment();
    List<ShipmentResponse> findTransitShipment();
    List<OrdersResponse> getAllOrdersForOneWeek();
}
