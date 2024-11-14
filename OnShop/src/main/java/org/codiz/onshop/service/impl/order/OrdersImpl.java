package org.codiz.onshop.service.impl.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.OrderItemsRequests;
import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.entities.orders.OrderItems;
import org.codiz.onshop.entities.orders.Orders;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.order.OrdersItemsRepository;
import org.codiz.onshop.repositories.order.OrdersRepository;
import org.codiz.onshop.repositories.products.ProductsJpaRepository;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersImpl {
    private final OrdersRepository ordersRepository;
    private final OrdersItemsRepository ordersItemsRepository;
    private final UsersRepository usersRepository;
    private final ProductsJpaRepository productsJpaRepository;



    public EntityResponse placeOrder(OrderPlacementRequest request) {

        Orders orders = new Orders();
        Users usr = usersRepository.findUsersByUserId(request.getUserId());
        orders.setUserId(usr);
        orders.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        orders.setOfficeAddress(request.getOfficeAddress());
        orders.setDoorStepAddress(orders.getOfficeAddress());
        Orders order = ordersRepository.save(orders);

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
        }
        order.setTotalAmount(totalAmount);
        ordersRepository.save(order);
        EntityResponse entityResponse = new EntityResponse();
        entityResponse.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entityResponse.setMessage("Order successfully placed");
        entityResponse.setStatus(HttpStatus.OK);
        return entityResponse;
    }
}
