package org.codiz.onshop.service.impl.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.ControllerAdvice.custom.EntityDeletionException;
import org.codiz.onshop.ControllerAdvice.custom.ResourceCreationFailedException;
import org.codiz.onshop.ControllerAdvice.custom.ResourceNotFoundException;
import org.codiz.onshop.dtos.requests.LocationRequest;
import org.codiz.onshop.dtos.requests.MakingOrderRequest;
import org.codiz.onshop.dtos.requests.OrderItemsRequests;
import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.orders.*;
import org.codiz.onshop.entities.products.PopularProducts;
import org.codiz.onshop.entities.products.SpecificProductDetails;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.cart.CartItemsRepository;
import org.codiz.onshop.repositories.order.OrdersItemsRepository;
import org.codiz.onshop.repositories.order.OrdersRepository;
import org.codiz.onshop.repositories.products.InventoryRepository;
import org.codiz.onshop.repositories.products.ProductsJpaRepository;
import org.codiz.onshop.repositories.products.SpecificProductsRepository;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.codiz.onshop.service.serv.orders.OrdersService;
import org.codiz.onshop.repositories.products.PopularProductsRepository;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

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
    private final ModelMapper modelMapper;
    private final SpecificProductsRepository specificProductsRepository;
    private final PopularProductsRepository popularProductsRepository;
    private final CartItemsRepository cartItemsRepository;


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

    @Transactional
    public HttpStatus makeOrder(List<MakingOrderRequest> request, String userId) {
        try {

            Users usr = usersRepository.findById(userId)
                    .orElseThrow(() -> new ResourceCreationFailedException("User not found"));

            // Find or create an order for the user
            Orders orders = ordersRepository.findByUserId(usr);
            if (orders == null) {
                orders = new Orders();
                orders.setUserId(usr);
                orders = ordersRepository.save(orders);
            }

            // Create and save order items
            List<OrderItems> orderItemsList = new ArrayList<>();
            /*int count = 1;*/
            for (MakingOrderRequest orderRequest : request) {
                OrderItems orderItems = new OrderItems();
                log.info("item id :" +orderRequest.getSpecificationId() + "quantity :"+orderRequest.getQuantity());
                PopularProducts popularProducts = new PopularProducts();

                SpecificProductDetails details;
                details = specificProductsRepository.findBySpecificProductId(orderRequest.getSpecificationId()).orElse(null);

                if (details == null){
                    details =  cartItemsRepository.findCartItemsByCartItemId(orderRequest.getSpecificationId()).getProducts();
                }

                if (ordersItemsRepository.existsOrderItemsBySpecificProductDetails(details)){
                    log.info("product already exists");
                    OrderItems items = ordersItemsRepository.findOrderItemsBySpecificProductDetails(details);
                    if (items.getStatus() == OrderItemStatus.CANCELLED){
                        items.setStatus(OrderItemStatus.ACTIVE);
                    }
                    return HttpStatus.OK;
                }
                orderItems.setSpecificProductDetails(details);
                orderItems.setQuantity(orderRequest.getQuantity());
                orderItems.setOrderId(orders);
                /*popularProducts.setSpecificProductDetails(details);
                popularProducts.setCount(count);*/

                orderItemsList.add(orderItems);
                //popularProductsRepository.save(popularProducts);

            }


            ordersItemsRepository.saveAll(orderItemsList);

            return HttpStatus.OK;
        } catch (NoSuchElementException e) {
            throw new ResourceCreationFailedException("User not found");
        } catch (ResourceCreationFailedException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceCreationFailedException("Could not make the order");
        }
    }


    @Transactional
    public EntityResponse placeOrder(OrderPlacementRequest request) {

        try {
            String longitude = request.getDoorStepAddress().getLongitude();
            String latitude = request.getDoorStepAddress().getLatitude();
            Orders orders = new Orders();
            Users usr = usersRepository.findUsersByUserId(request.getUserId());
            orders.setUserId(usr);
            orders.setCreatedOn(Instant.now());
            if (longitude != null && latitude != null) {
                orders.setLongitude(longitude);
                orders.setLatitude(latitude);
            }
            Orders order = ordersRepository.save(orders);

            List<OrderItems> orderItems = new ArrayList<>();


            Orders newOrder = ordersRepository.findByOrderId(order.getOrderId());
            List<OrderItems> orderItemsList = orders.getOrderItems();
            float amount = 0;
            for (OrderItems orderItem : orderItemsList) {
                amount += (orderItem.getSpecificProductDetails().getProductPrice() - orderItem
                        .getSpecificProductDetails().getDiscount()) * orderItem.getQuantity();
            }
            newOrder.setTotalAmount(amount);
            newOrder.setOrderItems(orderItems);
            newOrder.setOrderStatus(OrderStatus.UNDELIVERED);
            ordersRepository.save(newOrder);
            EntityResponse entityResponse = new EntityResponse();
            entityResponse.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            entityResponse.setMessage("Order successfully placed");
            entityResponse.setStatus(HttpStatus.OK);
            return entityResponse;
        }catch (Exception e) {
            throw new ResourceCreationFailedException("products could not be created");
        }
    }

    public HttpStatus removeOrderItems(String orderItemId,String userId) {
        try {
            OrderItems items = ordersItemsRepository.findOrderItemsByOrderItemId(orderItemId).get();
            if (!userId.equals(items.getOrderId().getUserId().getUserId())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            productsService.addProductQuantity(items.getSpecificProductDetails().getSpecificProductId(), items.getQuantity());
            items.setStatus(OrderItemStatus.CANCELLED);
            ordersItemsRepository.save(items);
            log.info("cancelled successfully");
            return HttpStatus.OK;
        }catch (Exception e) {
            throw new EntityDeletionException("could not remove the order item from orders");
        }

    }

    @Transactional
    public EntityDeletionResponse cancelOrder(String orderId, String username) {
        try {
            Orders orders = ordersRepository.findById(orderId).orElseThrow(
                    () -> new RuntimeException("product not found")
            );

            if (!username.equals(orders.getUserId().getUsername())){
                throw new RuntimeException("invalid request");
            }

            orders.setOrderStatus(OrderStatus.CANCELLED);

            List<OrderItems> items = new ArrayList<>();

            for (OrderItems orderItem : orders.getOrderItems()) {
                SpecificProductDetails productDetails = specificProductsRepository.findBySpecificProductId(orderItem.getSpecificProductDetails().getSpecificProductId())
                        .orElseThrow(()->new ResourceCreationFailedException("Product not found"));
                productDetails.setCount(productDetails.getCount() + orderItem.getQuantity());
                specificProductsRepository.save(productDetails);
                orderItem.setStatus(OrderItemStatus.CANCELLED);
                items.add(orderItem);
            }
            ordersItemsRepository.saveAll(items);




            EntityDeletionResponse entityDeletionResponse = new EntityDeletionResponse();
            entityDeletionResponse.setMessage("Order successfully cancelled");
            entityDeletionResponse.setStatus(HttpStatus.OK);
            entityDeletionResponse.setTimestamp(new Timestamp(System.currentTimeMillis()));
            return entityDeletionResponse;
        }catch (Exception e) {
            throw new EntityDeletionException("could not cancel the order");
        }
    }

    @Transactional
    public OrdersResponse getOrders(String orderId) {

        try {
            Orders orders = ordersRepository.findById(orderId).orElseThrow(()->new RuntimeException("order not found"));

            OrdersResponse response = new OrdersResponse();
            response.setOrderNumber(orders.getOrderId());
            response.setTotalCharges(orders.getTotalAmount());
            response.setOrderStatus(orders.getOrderStatus().toString());

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
                OrderItemsResponse itemsResponse = getOrderItemsResponse(items);
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


            response.setAddress(orders.getLongitude() + " " + orders.getLatitude());


            return response;
        }catch (Exception e){
            e.printStackTrace();
            throw new ResourceNotFoundException("could not find order");
        }


    }

    private static OrderItemsResponse getOrderItemsResponse(OrderItems items) {
        OrderItemsResponse itemsResponse = new OrderItemsResponse();
        itemsResponse.setProductImageUrl(items.getSpecificProductDetails().getProductImagesList().get(0).getImageUrl());
        itemsResponse.setProductName(items.getSpecificProductDetails().getProducts().getProductName());
        itemsResponse.setProductPrice(items.getSpecificProductDetails().getProductPrice());
        itemsResponse.setQuantity(items.getQuantity());
        itemsResponse.setTotalPrice(items.getTotalPrice());
        itemsResponse.setStatus(items.getStatus());

        return itemsResponse;
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
        try {
            Page<Orders> ordersList = ordersRepository.findAllByOrderByCreatedOnDesc(pageable);

            List<AllOrdersResponse> responses = new ArrayList<>();

            for (Orders order : ordersList) {
                AllOrdersResponse ordersResponse = getOrderResponse(order);
                responses.add(ordersResponse);

            }

            return new PageImpl<>(responses) ;
        }catch (Exception e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("could not find orders");
        }
    }


    private void getOrderItemsResponse(Orders order, AllOrdersResponse ordersResponse) {
        try {
            List<OrderItemsResponse> itemsResponses = new ArrayList<>();

            for (OrderItems orderItems : order.getOrderItems()) {
                OrderItemsResponse itemsResponse = new OrderItemsResponse();
                itemsResponse.setProductId(orderItems.getOrderItemId());
                itemsResponse.setProductName(orderItems.getSpecificProductDetails().getProducts().getProductName());
                itemsResponse.setQuantity(orderItems.getQuantity());
                itemsResponse.setProductImageUrl(orderItems.getSpecificProductDetails().getProductImagesList().get(0).getImageUrl());
                itemsResponse.setProductPrice((orderItems.getSpecificProductDetails().getProductPrice()-orderItems.getSpecificProductDetails().getDiscount()));
                itemsResponse.setTotalPrice(orderItems.getTotalPrice());
                itemsResponse.setStatus(orderItems.getStatus());
                itemsResponses.add(itemsResponse);
            }
            ordersResponse.setItems(itemsResponses);
        }catch (Exception e){
            throw new ResourceNotFoundException("could not find order Items");
        }
    }


    public List<AllOrdersResponse> getAllOrdersForOneWeek(){

        try {
            Instant oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
            Instant now = Instant.now();
            List<Orders> ordersList = ordersRepository.findAllByCreatedOnBetweenOrderByCreatedOnDesc(oneWeekAgo,now);

            List<AllOrdersResponse> ordersResponses = new ArrayList<>();
            for (Orders order : ordersList) {

                AllOrdersResponse response = getOrderResponse(order);
                ordersResponses.add(response);
            }
            return ordersResponses;
        }catch (Exception e) {
            throw new ResourceNotFoundException("could not find orders");
        }
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


        response.setOrderAddress(order.getLongitude()+","+order.getLatitude());

        getOrderItemsResponse(order, response);
        return response;
    }




    public Page<AllOrdersResponse> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        try {
            Page<Orders> ordersPage = ordersRepository.findAllByOrderStatusOrderByCreatedOnAsc(status, pageable);

            List<AllOrdersResponse> responses = ordersPage.getContent().stream()
                    .map(this::mapToAllOrdersResponse)
                    .toList();

            return new PageImpl<>(responses, pageable, ordersPage.getTotalElements());
        }catch (Exception e){
            throw new ResourceNotFoundException("could not get the orders");

        }
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


        response.setOrderAddress(order.getLongitude() + "," + order.getLatitude());


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
                orderItem.getSpecificProductDetails().getProductImagesList().isEmpty()
                        ? null
                        : orderItem.getSpecificProductDetails().getProductImagesList().get(0).getImageUrl()
        );
        response.setProductPrice(orderItem.getSpecificProductDetails().getProductPrice());
        response.setProductName(orderItem.getSpecificProductDetails().getProducts().getProductName());
        response.setQuantity(orderItem.getQuantity());
        return response;
    }


    public String updateStatus(String orderId, OrderStatus status){
        Orders orders = ordersRepository.findByOrderId(orderId);
        orders.setOrderStatus(status);
        ordersRepository.save(orders);
        return "order status updated successfully";
    }

    @Transactional
    public OrderStatusResponse getShippingStatus(String userId){
        try{
            Users usr = usersRepository.findUsersByUserId(userId);

            Orders orders = ordersRepository.findByUserId(usr);

            ShippingStatus status = orders.getShippingStatus();
            OrderStatusResponse orderStatus = new OrderStatusResponse();
            orderStatus.setOrderId(orders.getOrderId());

            if (status == ShippingStatus.SIGNED){
                orderStatus.setStatus(status.ordinal() + 1);
            }else {
                orderStatus.setStatus(status.ordinal());
            }

            List<OrderTrackingProducts> products = new ArrayList<>();
            log.info("filtering the itemsList");
            List<OrderItems> itemsList = orders.getOrderItems().stream().filter(
                    orderItems -> orderItems.getStatus() != OrderItemStatus.CANCELLED).toList();
            log.info("list filtered");

            for (OrderItems items : itemsList){
                OrderTrackingProducts orderTrackingProducts = new OrderTrackingProducts();
                orderTrackingProducts.setProductImageUrl(items.getSpecificProductDetails().getProductImagesList().get(0).getImageUrl());
                orderTrackingProducts.setProductPrice(items.getSpecificProductDetails().getProductPrice() - items.getSpecificProductDetails().getDiscount());
                orderTrackingProducts.setProductName(items.getSpecificProductDetails().getProducts().getProductName());
                orderTrackingProducts.setProductQuantity(items.getQuantity());
                orderTrackingProducts.setSpecificProductId(items.getOrderItemId());

                products.add(orderTrackingProducts);
            }
            orderStatus.setProducts(products);
            log.info("success in getting the order status");
            return orderStatus;
        }catch (Exception e){
            e.printStackTrace();
            throw new ResourceNotFoundException("could not find shipping status");
        }
    }


    public String updateShippingStatus(String orderId, ShippingStatus status){
        Orders orders = ordersRepository.findByOrderId(orderId);
        orders.setShippingStatus(status);
        ordersRepository.save(orders);
        return "shipping status updated successfully";
    }

    public ResponseEntity addOrderItemQuantity(String orderIteId, int quantity){
        try {
            OrderItems items = ordersItemsRepository.findOrderItemsByOrderItemId(orderIteId).get();
            int newQuantity = items.getQuantity() + quantity;
            items.setQuantity(newQuantity);
            items.setStatus(items.getStatus());
            items.setOrderId(items.getOrderId());
            float totalPrice = (items.getSpecificProductDetails().getProductPrice() - items.getSpecificProductDetails().getDiscount()) * newQuantity;
            items.setTotalPrice(totalPrice);
            items.setSpecificProductDetails(items.getSpecificProductDetails());
            return ResponseEntity.status(200).body("added successfully");
        }catch (Exception e){
            return ResponseEntity.status(500).body("could not add order item quantity");
        }
    }

    @Transactional
    public PaymentDetails getPaymentDetails(String userId){
        try {

            Users usr = usersRepository.findUsersByUserId(userId);
            Orders orders = ordersRepository.findByUserId(usr);
            if (orders == null){
                log.error("could not find payment details");
                return null;
            }

            PaymentDetails paymentDetails = new PaymentDetails();
            paymentDetails.setUsername(usr.getUsername());
            paymentDetails.setOrderId(orders.getOrderId());
            paymentDetails.setPhoneNumber(usr.getPhoneNumber());
            paymentDetails.setLocation(usr.getProfile().getAddress());

            List<PaymentProducts> products = new ArrayList<>();
            float totals = 0;
            for (OrderItems orderItems : orders.getOrderItems()) {
                PaymentProducts productDetails = new PaymentProducts();
                productDetails.setProductId(orderItems.getOrderItemId());
                productDetails.setProductName(orderItems.getSpecificProductDetails().getProducts().getProductName());
                productDetails.setProductCount(orderItems.getQuantity());
                productDetails.setProductImage(orderItems.getSpecificProductDetails().getProductImagesList().get(0).getImageUrl());
                float price = (orderItems.getSpecificProductDetails().getProductPrice() - orderItems.getSpecificProductDetails().getDiscount()) * orderItems.getQuantity();
                productDetails.setProductPrice(price);
                totals += price;
                products.add(productDetails);
            }
            paymentDetails.setProducts(products);

            paymentDetails.setProductsAmount(totals);
            paymentDetails.setShippingCost(0);
            log.info("success");

            return paymentDetails;

        }catch (Exception e){
            e.printStackTrace();
            throw new ResourceNotFoundException("could not find the payment details");
        }
    }

    public HttpStatus updateShippingDetails(String userId, LocationRequest locationRequest){
        try {
            Users usr = usersRepository.findUsersByUserId(userId);
            Orders orders = ordersRepository.findByUserId(usr);
            orders.setLongitude(locationRequest.getLongitude());
            orders.setLatitude(locationRequest.getLatitude());
            ordersRepository.save(orders);

            return HttpStatus.OK;
        }catch (Exception e){
            e.printStackTrace();
            throw new ResourceCreationFailedException("could not update shipping details");
        }
    }

    public HttpStatus updateShippingQuantity(String orderItemId, int quantity){
        try {
           OrderItems orderItems = ordersItemsRepository.findOrderItemsByOrderItemId(orderItemId).orElse(null);
           if (orderItems == null){
               return HttpStatus.OK;
           }
           orderItems.setQuantity(quantity);
           return HttpStatus.OK;
        }catch (Exception e){
            e.printStackTrace();
            throw new ResourceCreationFailedException("could not update shipping quantity");
        }
    }

    public SpecificOrderItemResponse getOrderItemDetails(String orderItemId){
        try {
            OrderItems orderItems = ordersItemsRepository.findOrderItemsByOrderItemId(orderItemId).orElse(null);
            if (orderItems == null){
                return null;
            }
            SpecificOrderItemResponse itemResponse = new SpecificOrderItemResponse();
            itemResponse.setActive(orderItems.getStatus() == OrderItemStatus.ACTIVE);
            itemResponse.setProductImageUrl(orderItems.getSpecificProductDetails().getProductImagesList().get(0).getImageUrl());
            itemResponse.setProportion(orderItems.getSpecificProductDetails().getSize());
            itemResponse.setVariety(orderItems.getSpecificProductDetails().getColor());
            itemResponse.setQuantity(orderItems.getQuantity());
            itemResponse.setProductName(orderItems.getSpecificProductDetails().getProducts().getProductName());
            itemResponse.setProductDescription(orderItems.getSpecificProductDetails().getProducts().getProductDescription());
            return itemResponse;
        }catch (Exception e){
            e.printStackTrace();
            throw new ResourceNotFoundException("could not get order item details");
        }
    }

}
