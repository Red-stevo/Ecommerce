package org.codiz.onshop.service.impl.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.OrderItemsRequests;
import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.requests.ShipmentRequest;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.orders.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

        String longitude = request.getDoorStepAddress().getLongitude();
        String latitude = request.getDoorStepAddress().getLatitude();
        Orders orders = new Orders();
        Users usr = usersRepository.findUsersByUserId(request.getUserId());
        orders.setUserId(usr);
        orders.setCreatedOn(Instant.now());
        orders.setOfficeAddress(request.getOfficeAddress());
        if (longitude != null && latitude != null) {
            orders.setLongitude(longitude);
            orders.setLatitude(latitude);
        }
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
            //productsService.reduceProductQuantity(itemsRequests.getProductId(), itemsRequests.getQuantity());

        }
        Orders newOrder = ordersRepository.findByOrderId(order.getOrderId());
        newOrder.setTotalAmount(totalAmount);
        newOrder.setOrderItems(orderItems);
        ordersRepository.save(newOrder);
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

    public EntityDeletionResponse cancelOrder(String orderId, String username) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow(
                () -> new RuntimeException("product not found")
        );

        orders.setOrderStatus(OrderStatus.CANCELLED);

        for (OrderItems orderItems : orders.getOrderItems()) {

            Inventory inventory = inventoryRepository.findByProducts(orderItems.getProductId());
            inventory.setQuantitySold(inventory.getQuantitySold() - orderItems.getQuantity());
            inventoryRepository.save(inventory);
        }


        EntityDeletionResponse entityDeletionResponse = new EntityDeletionResponse();
        entityDeletionResponse.setMessage("Order successfully cancelled");
        entityDeletionResponse.setStatus(HttpStatus.OK);
        entityDeletionResponse.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return entityDeletionResponse;
    }

    @Transactional
    public OrdersResponse getOrders(String orderId) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow(()->new RuntimeException("order not found"));

        OrdersResponse response = new OrdersResponse();
        response.setOrderNumber(orders.getOrderId());
        response.setTotalCharges(orders.getTotalAmount());
        //response.setOrderStatus(orders.getOrderStatus().toString());

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerEmail(orders.getUserId().getUserEmail());
        customerDetails.setCustomerPhone(orders.getUserId().getPhoneNumber());
        customerDetails.setCustomerName(orders.getUserId().getUsername());
        customerDetails.setOrderNumber(orders.getOrderId());
        int itemsOrdered = orders.getOrderItems().stream()
                        .mapToInt(OrderItems::getQuantity).sum();
        customerDetails.setNumberOfItemsOrdered(itemsOrdered);

        response.setCustomerDetails(customerDetails);

        List<OrderItemsResponse> orderItemResponses = new ArrayList<>();
        for (OrderItems items : orders.getOrderItems()){
            OrderItemsResponse itemsResponse = new OrderItemsResponse();
            itemsResponse.setProductImageUrl(items.getProductId().getProductImagesList().get(0).getImageUrl());
            itemsResponse.setProductName(items.getProductId().getProductName());
            itemsResponse.setProductPrice(items.getProductId().getProductPrice());
            itemsResponse.setQuantity(items.getQuantity());
            itemsResponse.setTotalPrice(items.getTotalPrice());
            orderItemResponses.add(itemsResponse);
        }

        response.setItemsList(orderItemResponses);

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate createdDate = orders.getCreatedOn().atZone(zoneId).toLocalDate();
        LocalTime timeOrdered = orders.getCreatedOn().atZone(zoneId).toLocalTime();
        String dateCreated = formatOrderDate(createdDate);
        String orderTime = formatOrderTime(timeOrdered);

        OrderSummary orderSummary = new OrderSummary();
        orderSummary.setOrderDate(dateCreated);
        orderSummary.setOrderTime(orderTime);
        orderSummary.setOrderTotal((float) orders.getTotalAmount());
        orderSummary.setDeliveryFee(0);

        response.setOrderSummary(orderSummary);

        if (orders.getOfficeAddress() != null) {
            response.setAddress(orders.getOfficeAddress());
        }else {
            response.setAddress(orders.getLongitude() + " " + orders.getLatitude());
        }

        return response;



    }
    private String formatOrderDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM EEE dd yyyy", Locale.ENGLISH);
        return date.format(formatter);
    }
    private String formatOrderTime(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
        return time.format(formatter);
    }



    @Transactional
    public  Page<AllOrdersResponse> getAllOrdersGroupedByDate(Pageable pageable) {
        Page<Orders> ordersList = ordersRepository.findAllByOrderByCreatedOnDesc(pageable);

        List<AllOrdersResponse> responses = new ArrayList<>();

        for (Orders order : ordersList) {
            AllOrdersResponse ordersResponse = getOrderResponse(order);
            responses.add(ordersResponse);

        }

        return new PageImpl<>(responses) ;
    }


    private void getOrderItemsResponse(Orders order, AllOrdersResponse ordersResponse) {
        List<OrderItemsResponse> itemsResponses = new ArrayList<>();

        for (OrderItems orderItems : order.getOrderItems()) {
            OrderItemsResponse itemsResponse = new OrderItemsResponse();
            itemsResponse.setProductName(orderItems.getProductId().getProductName());
            itemsResponse.setQuantity(orderItems.getQuantity());
            itemsResponse.setProductImageUrl(orderItems.getProductId().getProductImagesList().get(0).getImageUrl());
            itemsResponse.setProductPrice((orderItems.getProductId().getProductPrice()-orderItems.getProductId().getDiscount()));
            itemsResponse.setTotalPrice(orderItems.getTotalPrice());
            itemsResponses.add(itemsResponse);
        }
        ordersResponse.setItems(itemsResponses);
    }


    public List<AllOrdersResponse> getAllOrdersForOneWeek(){

        Instant oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
        Instant now = Instant.now();
        List<Orders> ordersList = ordersRepository.findAllByCreatedOnBetweenOrderByCreatedOnDesc(oneWeekAgo,now);

        List<AllOrdersResponse> ordersResponses = new ArrayList<>();
        for (Orders order : ordersList) {

            AllOrdersResponse response = getOrderResponse(order);
            ordersResponses.add(response);
        }
        return ordersResponses;
    }

    private AllOrdersResponse getOrderResponse(Orders order) {
        AllOrdersResponse response = new AllOrdersResponse();
        response.setOrderId(order.getOrderId());
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate createdDate = order.getCreatedOn().atZone(zoneId).toLocalDate();
        String dateCreated = formatOrderDate(createdDate);
        response.setOrderDate(dateCreated);
        response.setOrderTotal((float) order.getTotalAmount());
        //response.setOrderStatus(order.getOrderStatus().toString());
        if (order.getOfficeAddress() != null) {
            response.setOrderAddress(order.getOfficeAddress());
        }else {
            response.setOrderAddress(order.getLongitude()+","+order.getLatitude());
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

    public Page<AllOrdersResponse> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findAllByOrderStatusOrderByCreatedOnAsc(status, pageable);

        List<AllOrdersResponse> responses = ordersPage.getContent().stream()
                .map(this::mapToAllOrdersResponse)
                .toList();

        return new PageImpl<>(responses, pageable, ordersPage.getTotalElements());
    }

    public Page<AllOrdersResponse> getDeliveredOrders(Pageable pageable) {
        return getOrdersByStatus(OrderStatus.DELIVERED, pageable);
    }

    public Page<AllOrdersResponse> getUndeliveredOrders(Pageable pageable) {
        return getOrdersByStatus(OrderStatus.UNDELIVERED, pageable);
    }

    public Page<AllOrdersResponse> getShippingOrders(Pageable pageable) {
        return getOrdersByStatus(OrderStatus.SHIPPING, pageable);
    }
    public Page<AllOrdersResponse> getCancelledOrders(Pageable pageable){
        return getOrdersByStatus(OrderStatus.CANCELLED,pageable);
    }

    private AllOrdersResponse mapToAllOrdersResponse(Orders order) {
        AllOrdersResponse response = new AllOrdersResponse();
        response.setOrderId(order.getOrderId());
        response.setOrderStatus(order.getOrderStatus().toString());

        if (order.getOfficeAddress() != null) {
            response.setOrderAddress(order.getOfficeAddress());
        } else {
            response.setOrderAddress(order.getLongitude() + "," + order.getLatitude());
        }

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = order.getCreatedOn().atZone(zoneId).toLocalDate();
        response.setOrderDate(formatOrderDate(localDate));

        List<OrderItemsResponse> items = order.getOrderItems().stream()
                .map(this::mapToOrderItemsResponse)
                .toList();

        response.setItems(items);
        return response;
    }

    private OrderItemsResponse mapToOrderItemsResponse(OrderItems orderItem) {
        OrderItemsResponse response = new OrderItemsResponse();
        response.setProductImageUrl(
                orderItem.getProductId().getProductImagesList().isEmpty()
                        ? null
                        : orderItem.getProductId().getProductImagesList().get(0).getImageUrl()
        );
        response.setProductPrice(orderItem.getProductId().getProductPrice());
        response.setProductName(orderItem.getProductId().getProductName());
        response.setQuantity(orderItem.getQuantity());
        return response;
    }

    public String deleteOrder(String orderId){
        Orders orders = ordersRepository.findByOrderId(orderId);
        ordersRepository.delete(orders);
        return "order deleted successfully";
    }

    public String updateStatus(String orderId, OrderStatus status){
        Orders orders = ordersRepository.findByOrderId(orderId);
        orders.setOrderStatus(status);
        ordersRepository.save(orders);
        return "order status updated successfully";
    }



}
