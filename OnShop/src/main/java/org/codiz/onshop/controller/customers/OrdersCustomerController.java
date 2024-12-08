package org.codiz.onshop.controller.customers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.service.serv.orders.OrdersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/costumer/orders")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin( origins = "http://127.0.0.1:5173/", allowCredentials = "true")
public class OrdersCustomerController {
    private final OrdersService ordersService;

    @PostMapping("/place-order")
    public ResponseEntity<EntityResponse> placeOrder(@RequestBody OrderPlacementRequest request){
        return ResponseEntity.ok(ordersService.placeOrder(request));
    }

    @PutMapping("/cancel-order-item")
    public ResponseEntity<String> removeOrderItems(@RequestParam String orderItemId, @RequestParam String userId){
        return ResponseEntity.ok(ordersService.removeOrderItems(orderItemId, userId));
    }

    @PutMapping("/cancel-order")
    public ResponseEntity<EntityDeletionResponse> cancelOrder(@RequestParam String orderId, @RequestParam String username){
        return ResponseEntity.ok(ordersService.cancelOrder(orderId, username));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrdersResponse> getOrder(@PathVariable String orderId){
        return ResponseEntity.ok(ordersService.getOrders(orderId));
    }





    @GetMapping("/get-order-status")
    public ResponseEntity<OrderStatusResponse> getOrderStatus(@RequestParam String userId){
        return ResponseEntity.ok(ordersService.getShippingStatus(userId));
    }




}
