package org.codiz.onshop.controller;

import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.CartItemsToAdd;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.codiz.onshop.service.serv.cart.CartService;
import org.codiz.onshop.service.serv.cart.CartsItemsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    private final CartsItemsService cartsItemsService;

    public CartController(CartService cartService, CartsItemsService cartsItemsService) {
        this.cartService = cartService;
        this.cartsItemsService = cartsItemsService;
    }

    @PostMapping
    public ResponseEntity<EntityCreationResponse> createCart(String userId){
        EntityCreationResponse response = cartService.createCart(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add/cart-item")
    public ResponseEntity<EntityCreationResponse> addCartItem(CartItemsToAdd itemsToAdd){
        EntityCreationResponse response = cartsItemsService.addItemToCart(itemsToAdd);
        return ResponseEntity.ok(response);
    }
}
