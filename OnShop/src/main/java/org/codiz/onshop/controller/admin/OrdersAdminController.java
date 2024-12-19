package org.codiz.onshop.controller.admin;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.ShippingStatusUpdateModel;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.orders.OrderStatus;
import org.codiz.onshop.entities.orders.ShippingStatus;
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
@RequestMapping("/api/v1/admin/orders")
@Slf4j
@RequiredArgsConstructor
public class OrdersAdminController {
    private final OrdersService ordersService;


    @GetMapping("/all")
    public ResponseEntity<PagedModel<EntityModel<AllOrdersResponse>>>
    getAllOrdersGroupedByDate(@RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              PagedResourcesAssembler<AllOrdersResponse> assembler) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AllOrdersResponse>  responses = ordersService.getAllOrdersGroupedByDate(pageable);
        PagedModel<EntityModel<AllOrdersResponse>> pagedModel = assembler.toModel(responses);
        return ResponseEntity.ok(pagedModel);
    }


    @GetMapping("/delivered")
    public ResponseEntity<PagedModel<EntityModel<AllOrdersResponse>>>
    getDeliveredOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       PagedResourcesAssembler<AllOrdersResponse> assembler){
        Pageable pageable = PageRequest.of(page, size);
        Page<AllOrdersResponse>  responses = ordersService.getDeliveredOrders(pageable);
        PagedModel<EntityModel<AllOrdersResponse>> pagedModel = assembler.toModel(responses);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/undelivered")
    public ResponseEntity<PagedModel<EntityModel<AllOrdersResponse>>>
    getUndeliveredOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size,
                         PagedResourcesAssembler<AllOrdersResponse> assembler){
        Pageable pageable = PageRequest.of(page, size);
        Page<AllOrdersResponse>  responses = ordersService.getUndeliveredOrders(pageable);
        PagedModel<EntityModel<AllOrdersResponse>> pagedModel = assembler.toModel(responses);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/shipping")
    public ResponseEntity<PagedModel<EntityModel<AllOrdersResponse>>>
    getShipping(@RequestParam(value = "page", defaultValue = "0") int page,
                @RequestParam(value = "size", defaultValue = "10") int size,
                PagedResourcesAssembler<AllOrdersResponse> assembler){
        Pageable pageable = PageRequest.of(page, size);
        Page<AllOrdersResponse>  responses = ordersService.getShippingOrders(pageable);
        PagedModel<EntityModel<AllOrdersResponse>> pagedModel = assembler.toModel(responses);
        return ResponseEntity.ok(pagedModel);
    }
    @GetMapping("/cancelled")
    public ResponseEntity<PagedModel<EntityModel<AllOrdersResponse>>>
    cancelledOrders(@RequestParam(value = "page", defaultValue = "0") int page,
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



    @PutMapping("/update-shipping-status/{orderId}")
    public ResponseEntity<String> updateShippingStatus(
            @PathVariable String orderId, @RequestBody ShippingStatusUpdateModel status){
        log.info("details :" +status);
        return ResponseEntity.ok(ordersService.updateShippingStatus(orderId, status.getStatus()));
    }



}
