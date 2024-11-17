package org.codiz.onshop.service.impl.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.OrderItemsRequests;
import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.response.EntityDeletionResponse;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.dtos.response.OrderItemsResponse;
import org.codiz.onshop.dtos.response.OrdersResponse;
import org.codiz.onshop.entities.orders.OrderItems;
import org.codiz.onshop.entities.orders.Orders;
import org.codiz.onshop.entities.products.Inventory;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.order.OrdersItemsRepository;
import org.codiz.onshop.repositories.order.OrdersRepository;
import org.codiz.onshop.repositories.products.InventoryRepository;
import org.codiz.onshop.repositories.products.ProductsJpaRepository;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.codiz.onshop.service.serv.orders.OrdersService;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
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



    public EntityResponse placeOrder(OrderPlacementRequest request) {

        Orders orders = new Orders();
        Users usr = usersRepository.findUsersByUserId(request.getUserId());
        orders.setUserId(usr);
        orders.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        orders.setOfficeAddress(request.getOfficeAddress());
        orders.setDoorStepAddress(orders.getOfficeAddress());
        Orders order = ordersRepository.save(orders);

        List<OrderItems> orderItems = new ArrayList<>();

        double totalAmount = 0;
        for (OrderItemsRequests itemsRequests: request.getRequestsList()){

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

    public String removeOrderItems(String orderItemId){
        OrderItems items = ordersItemsRepository.findOrderItemsByOrderItemId(orderItemId).get();
        productsService.addProductQuantity(items.getProductId().getProductId(), items.getQuantity());
        ordersItemsRepository.delete(items);
        return "item successfully removed";

    }

    public EntityDeletionResponse cancelOrder(String orderId){
        Orders orders = ordersRepository.findById(orderId).orElseThrow(
                ()-> new RuntimeException("product not found")
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

    public List<OrdersResponse> getOrders(String userId){
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

        List<OrderItemsResponse> itemsResponses = new ArrayList<>();
        for (OrderItems orderItems : orders.getOrderItems()) {
                OrderItemsResponse itemsResponse = new OrderItemsResponse();
                itemsResponse.setProductId(orderItems.getProductId());
                itemsResponse.setQuantity(orderItems.getQuantity());
                itemsResponse.setTotalPrice(orderItems.getTotalPrice());
                itemsResponses.add(itemsResponse);
        }
        ordersResponse.setItemsList(itemsResponses);
        return ordersResponse;
    }

    public Map<LocalDate, List<OrdersResponse>> getAllOrdersGroupedByDate() {
        List<Orders> ordersList = ordersRepository.findAll();


        Map<LocalDate, List<OrdersResponse>> ordersByDate = new TreeMap<>(Collections.reverseOrder());


        for (Orders order : ordersList) {
            OrdersResponse ordersResponse = OrderResponse(order);

            LocalDate orderDate = order.getCreatedOn().toLocalDateTime().toLocalDate();
            ordersByDate.computeIfAbsent(orderDate, k -> new ArrayList<>()).add(ordersResponse);
        }

        return ordersByDate;
    }

}
