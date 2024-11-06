package org.codiz.onshop.controller;

import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.codiz.onshop.service.serv.products.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<EntityCreationResponse> createCart(String userId){
        EntityCreationResponse response = cartService.createCart(userId);
        return ResponseEntity.ok(response);
    }
}
