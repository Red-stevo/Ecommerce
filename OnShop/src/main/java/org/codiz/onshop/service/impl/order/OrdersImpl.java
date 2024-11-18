package org.codiz.onshop.service.impl.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.OrderItemsRequests;
import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.requests.ShipmentRequest;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.orders.OrderItems;
import org.codiz.onshop.entities.orders.OrderShipping;
import org.codiz.onshop.entities.orders.Orders;
import org.codiz.onshop.entities.orders.ShippingStatus;
import org.codiz.onshop.entities.products.Inventory;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.order.OrdersItemsRepository;
import org.codiz.onshop.repositories.order.OrdersRepository;
import org.codiz.onshop.repositories.order.OrdersShippingRepository;
import org.codiz.onshop.repositories.products.InventoryRepository;
import org.codiz.onshop.repositories.products.ProductsJpaRepository;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.codiz.onshop.service.serv.orders.OrdersService;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersImpl implements OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrdersItemsRepository ordersItemsRepository;
    private final UsersRepository usersRepository;
    private final ProductsJpaRepository productsJpaRepository;
    private final ProductsService productsService;
    private final InventoryRepository inventoryRepository;
    private final OrdersShippingRepository shippingRepository;
    private final ModelMapper modelMapper;

    public String generateShipmentTracingId() {

        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int ID_LENGTH = 20;
        final SecureRandom RANDOM = new SecureRandom();

        StringBuilder builder = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            builder.append(CHARACTERS.charAt(randomIndex));
        }
        return builder.toString();
    }

    public EntityResponse placeOrder(OrderPlacementRequest request) {

        Orders orders = new Orders();
        Users usr = usersRepository.findUsersByUserId(request.getUserId());
        orders.setUserId(usr);
        orders.setCreatedOn(Instant.now());
        orders.setOfficeAddress(request.getOfficeAddress());
        orders.setDoorStepAddress(orders.getOfficeAddress());
        Orders order = ordersRepository.save(orders);

        List<OrderItems> orderItems = new ArrayList<>();

        double totalAmount = 0;
        for (OrderItemsRequests itemsRequests : request.getRequestsList()) {

            Products products = productsJpaRepository.findByProductId(itemsRequests.getProductId());
            OrderItems items = new OrderItems();
            items.setOrderId(order);
            items.setQuantity(itemsRequests.getQuantity());
            items.setProductId(products);

            double totalPrice = products.getProductPrice() * itemsRequests.getQuantity();
            items.setTotalPrice(totalPrice);
            totalAmount += totalPrice;
            ordersItemsRepository.save(items);
            orderItems.add(items);
            productsService.reduceProductQuantity(itemsRequests.getProductId(), itemsRequests.getQuantity());

        }
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
        ordersRepository.save(order);
        EntityResponse entityResponse = new EntityResponse();
        entityResponse.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entityResponse.setMessage("Order successfully placed");
        entityResponse.setStatus(HttpStatus.OK);
        return entityResponse;
    }

    public String removeOrderItems(String orderItemId) {
        OrderItems items = ordersItemsRepository.findOrderItemsByOrderItemId(orderItemId).get();
        productsService.addProductQuantity(items.getProductId().getProductId(), items.getQuantity());
        ordersItemsRepository.delete(items);
        return "item successfully removed";

    }

    public EntityDeletionResponse cancelOrder(String orderId) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow(
                () -> new RuntimeException("product not found")
        );

        for (OrderItems orderItems : orders.getOrderItems()) {

            Inventory inventory = inventoryRepository.findByProducts(orderItems.getProductId());
            inventory.setQuantitySold(inventory.getQuantitySold() - orderItems.getQuantity());
            inventoryRepository.save(inventory);
        }
        ordersRepository.delete(orders);

        EntityDeletionResponse entityDeletionResponse = new EntityDeletionResponse();
        entityDeletionResponse.setMessage("Order successfully cancelled");
        entityDeletionResponse.setStatus(HttpStatus.OK);
        entityDeletionResponse.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return entityDeletionResponse;
    }

    public List<OrdersResponse> getOrders(String userId) {
        Users users = usersRepository.findUsersByUserId(userId);
        List<Orders> ordersList = ordersRepository.findAllByUserIdOrderByCreatedOnDesc(users);
        List<OrdersResponse> ordersResponses = new ArrayList<>();
        for (Orders orders : ordersList) {
            OrdersResponse ordersResponse = OrderResponse(orders);
            /*OrdersResponse ordersResponse;*/
            ordersResponses.add(ordersResponse);
        }
        return ordersResponses;
    }

    private OrdersResponse OrderResponse(Orders orders) {
        OrdersResponse ordersResponse = new OrdersResponse();
        ordersResponse.setOrderDate(orders.getCreatedOn());
        ordersResponse.setAddress(orders.getOfficeAddress());
        ordersResponse.setTotalPrice(orders.getTotalAmount());

        getOrderItemsResponse(orders, ordersResponse);
        return ordersResponse;
    }

    public Map<LocalDate, List<OrdersResponse>> getAllOrdersGroupedByDate() {
        List<Orders> ordersList = ordersRepository.findAll();

        // Reverse order map to ensure the latest dates come first
        Map<LocalDate, List<OrdersResponse>> ordersByDate = new TreeMap<>(Collections.reverseOrder());


        ZoneId zoneId = ZoneId.systemDefault();

        for (Orders order : ordersList) {
            OrdersResponse ordersResponse = getOrderResponse(order);

            LocalDate orderDate = order.getCreatedOn().atZone(zoneId).toLocalDate();

            ordersByDate.computeIfAbsent(orderDate, k -> new ArrayList<>()).add(ordersResponse);
        }

        return ordersByDate;
    }

    private void getOrderItemsResponse(Orders order, OrdersResponse ordersResponse) {
        List<OrderItemsResponse> itemsResponses = new ArrayList<>();
        for (OrderItems orderItems : order.getOrderItems()) {
            OrderItemsResponse itemsResponse = new OrderItemsResponse();
            itemsResponse.setProductId(orderItems.getProductId());
            itemsResponse.setQuantity(orderItems.getQuantity());
            itemsResponse.setTotalPrice(orderItems.getTotalPrice());
            itemsResponses.add(itemsResponse);
        }
        ordersResponse.setItemsList(itemsResponses);
    }


    public List<OrdersResponse> getAllOrdersForOneWeek(){

        Instant oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
        Instant now = Instant.now();
        List<Orders> ordersList = ordersRepository.findAllByCreatedOnBetweenOrderByCreatedOnDesc(oneWeekAgo,now);

        List<OrdersResponse> ordersResponses = new ArrayList<>();
        for (Orders order : ordersList) {

            OrdersResponse response = getOrderResponse(order);
            ordersResponses.add(response);
        }
        return ordersResponses;
    }

    private OrdersResponse getOrderResponse(Orders order) {
        OrdersResponse response = new OrdersResponse();
        response.setOrderDate(order.getCreatedOn());
        response.setTotalPrice(order.getTotalAmount());
        if (order.getOfficeAddress() != null) {
            response.setAddress(order.getOfficeAddress());
        }else {
            response.setAddress(order.getDoorStepAddress());
        }
        getOrderItemsResponse(order, response);
        return response;
    }


    public EntityResponse createShipment(ShipmentRequest request){

        Orders orders = ordersRepository.findByOrderId(request.getOrderId());
        OrderShipping shipping = new OrderShipping();
        shipping.setShippingDate(request.getShipDate());
        shipping.setDeliveryDate(request.getDeliveryDate());
        shipping.setShippingStatus(request.getStatus());
        shipping.setTrackingId(generateShipmentTracingId());
        shipping.setOrder(orders);

        EntityResponse entityResponse = new EntityResponse();
        entityResponse.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entityResponse.setMessage("Shipment successfully created");
        entityResponse.setStatus(HttpStatus.OK);
        return entityResponse;

    }

    public EntityResponse updateShipment(String trackingId, ShipmentRequest request){

        OrderShipping shipping = shippingRepository.findByTrackingId(trackingId);
        shipping.setShippingDate(request.getShipDate());
        shipping.setDeliveryDate(request.getDeliveryDate());
        shipping.setShippingStatus(request.getStatus());
        shipping.setTrackingId(trackingId);
        shippingRepository.save(shipping);

        EntityResponse entityResponse = new EntityResponse();
        entityResponse.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entityResponse.setMessage("Shipment successfully updated");
        entityResponse.setStatus(HttpStatus.OK);

        return entityResponse;
    }

    public ShipmentResponse getShipment(String trackingId){
        OrderShipping shipping = shippingRepository.findByTrackingId(trackingId);
        return modelMapper.map(shipping, ShipmentResponse.class);
    }

    public List<ShipmentResponse> getAllShipments(){
        List<OrderShipping> shippingList = shippingRepository.findAll();
        return getShipmentResponses(shippingList);
    }
    public String deleteShipment(String trackingId){

        OrderShipping shipping = shippingRepository.findByTrackingId(trackingId);
        shippingRepository.delete(shipping);
        return "Shipment successfully deleted";
    }

    public List<ShipmentResponse> findProcessingShipment(){
        List<OrderShipping> shippingList = shippingRepository.findAllByShippingStatusEquals(ShippingStatus.PROCESSING);
        return getShipmentResponses(shippingList);
    }
    public List<ShipmentResponse> findDeliveredShipment(){
        List<OrderShipping> shippingList = shippingRepository.findAllByShippingStatusEquals(ShippingStatus.DELIVERED);
        return getShipmentResponses(shippingList);
    }
    public List<ShipmentResponse> findReturnedShipment(){
        List<OrderShipping> shippingList = shippingRepository.findAllByShippingStatusEquals(ShippingStatus.RETURNED);
        return getShipmentResponses(shippingList);
    }
    public List<ShipmentResponse> findTransitShipment(){
        List<OrderShipping> shippingList = shippingRepository.findAllByShippingStatusEquals(ShippingStatus.TRANSIT);
        return getShipmentResponses(shippingList);
    }



    private List<ShipmentResponse> getShipmentResponses(List<OrderShipping> shippingList) {
        List<ShipmentResponse> shipmentResponses = new ArrayList<>();
        for (OrderShipping shipping : shippingList) {
            ShipmentResponse shipmentResponse = new ShipmentResponse();
            shipmentResponse.setTrackingId(shipping.getTrackingId());
            shipmentResponse.setShipDate(shipping.getShippingDate());
            shipmentResponse.setShippingStatus(shipping.getShippingStatus());
            shipmentResponse.setDeliveryDate(shipping.getDeliveryDate());

            shipmentResponses.add(shipmentResponse);
        }
        return shipmentResponses;
    }
}
