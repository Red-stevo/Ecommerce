package org.codiz.onshop.controller.customers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.CancelOrderRequest;
import org.codiz.onshop.dtos.requests.LocationRequest;
import org.codiz.onshop.dtos.requests.MakingOrderRequest;
import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.service.serv.orders.OrdersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/costumer/orders")
@Slf4j
@RequiredArgsConstructor
public class OrdersCustomerController {
    private final OrdersService ordersService;

    @PostMapping("/place-order")
    public ResponseEntity<EntityResponse> placeOrder(@RequestBody OrderPlacementRequest request){
        return ResponseEntity.ok(ordersService.placeOrder(request));
    }

    @PutMapping("/cancel-order-item")
    public ResponseEntity<HttpStatus> removeOrderItems(@RequestBody CancelOrderRequest data){
        log.info("request to cancel order");
        return ResponseEntity.ok(ordersService.removeOrderItems(data.getOrderItemId(), data.getUserId()));
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrdersResponse> getOrder(@PathVariable String orderId){
        return ResponseEntity.ok(ordersService.getOrders(orderId));
    }

    @GetMapping("/get-order-status") /*Done*/
    public ResponseEntity<OrderStatusResponse> getOrderStatus(@RequestParam String userId){
        log.info("user is :" + userId);
        return ResponseEntity.ok(ordersService.getShippingStatus(userId));
    }

    @PutMapping("/update-order-quantity")
    public ResponseEntity<ResponseEntity> addOrderItemQuantity(@RequestParam String orderIteId, @RequestParam int quantity){
        return ResponseEntity.ok(ordersService.addOrderItemQuantity(orderIteId,quantity));
    }

    @PostMapping("/make-order")
    public ResponseEntity<HttpStatus> makeOrder(@RequestBody List<MakingOrderRequest> request, @RequestParam String userId){
        return ResponseEntity.ok(ordersService.makeOrder(request,userId));
    }

    @GetMapping("/payment-details/{userId}")
    public ResponseEntity<PaymentDetails> getCustomerDetails(@PathVariable String userId){
        return ResponseEntity.ok(ordersService.getPaymentDetails(userId));
    }

    @PutMapping("/update-shipping-details/{userId}")
    public ResponseEntity<HttpStatus> updateShippingDetails(@PathVariable String userId, @RequestBody LocationRequest locationRequest){
        return ResponseEntity.ok(ordersService.updateShippingDetails(userId,locationRequest));
    }

    @PutMapping("/update-order-quantity/{orderItemId}")
    public ResponseEntity<HttpStatus> updateShippingQuantity(@PathVariable String orderItemId, @RequestParam int quantity){
        return ResponseEntity.ok(ordersService.updateShippingQuantity(orderItemId,quantity));
    }
}
