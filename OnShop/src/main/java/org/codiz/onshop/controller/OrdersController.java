package org.codiz.onshop.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.service.serv.orders.OrdersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    @PostMapping("/place-order")
    public ResponseEntity<EntityResponse> placeOrder(OrderPlacementRequest request){
        return ResponseEntity.ok(ordersService.placeOrder(request));
    }
}
