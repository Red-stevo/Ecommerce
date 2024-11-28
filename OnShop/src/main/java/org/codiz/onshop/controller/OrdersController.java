package org.codiz.onshop.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.OrderPlacementRequest;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.orders.OrderStatus;
import org.codiz.onshop.service.serv.orders.OrdersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin( origins = "http://127.0.0.1:5173/", allowCredentials = "true")
public class OrdersController {
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

    @GetMapping("/all")
    public ResponseEntity<PagedModel<EntityModel<AllOrdersResponse>>> getAllOrdersGroupedByDate(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                   PagedResourcesAssembler<AllOrdersResponse> assembler) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AllOrdersResponse>  responses = ordersService.getAllOrdersGroupedByDate(pageable);
        PagedModel<EntityModel<AllOrdersResponse>> pagedModel = assembler.toModel(responses);
        return ResponseEntity.ok(pagedModel);
    }


    @GetMapping("/delivered")
    public ResponseEntity<PagedModel<EntityModel<AllOrdersResponse>>> getDeliveredOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                      @RequestParam(value = "size", defaultValue = "10") int size,
                                                                      PagedResourcesAssembler<AllOrdersResponse> assembler){
        Pageable pageable = PageRequest.of(page, size);
        Page<AllOrdersResponse>  responses = ordersService.getDeliveredOrders(pageable);
        PagedModel<EntityModel<AllOrdersResponse>> pagedModel = assembler.toModel(responses);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/undelivered")
    public ResponseEntity<PagedModel<EntityModel<AllOrdersResponse>>> getUndeliveredOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                      @RequestParam(value = "size", defaultValue = "10") int size,
                                                                      PagedResourcesAssembler<AllOrdersResponse> assembler){
        Pageable pageable = PageRequest.of(page, size);
        Page<AllOrdersResponse>  responses = ordersService.getUndeliveredOrders(pageable);
        PagedModel<EntityModel<AllOrdersResponse>> pagedModel = assembler.toModel(responses);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/shipping")
    public ResponseEntity<PagedModel<EntityModel<AllOrdersResponse>>> getShipping(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                      @RequestParam(value = "size", defaultValue = "10") int size,
                                                                      PagedResourcesAssembler<AllOrdersResponse> assembler){
        Pageable pageable = PageRequest.of(page, size);
        Page<AllOrdersResponse>  responses = ordersService.getShippingOrders(pageable);
        PagedModel<EntityModel<AllOrdersResponse>> pagedModel = assembler.toModel(responses);
        return ResponseEntity.ok(pagedModel);
    }
    @GetMapping("/cancelled")
    public ResponseEntity<PagedModel<EntityModel<AllOrdersResponse>>> cancelledOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                      @RequestParam(value = "size", defaultValue = "10") int size,
                                                                      PagedResourcesAssembler<AllOrdersResponse> assembler){
        Pageable pageable = PageRequest.of(page, size);
        Page<AllOrdersResponse>  responses = ordersService.getCancelledOrders(pageable);
        PagedModel<EntityModel<AllOrdersResponse>> pagedModel = assembler.toModel(responses);
        return ResponseEntity.ok(pagedModel);
    }

    @PutMapping("/update/status")
    public ResponseEntity<String> updateStatus(@RequestParam String orderId, @RequestParam OrderStatus status){
        String result = ordersService.updateStatus(orderId, status);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/get-order-status")
    public ResponseEntity<OrderStatusResponse> getOrderStatus(@RequestParam String orderId){
        return ResponseEntity.ok(ordersService.getOrderStatus(orderId));
    }



}
