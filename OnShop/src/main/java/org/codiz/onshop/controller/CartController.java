package org.codiz.onshop.controller;

import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.CartItemsDeletion;
import org.codiz.onshop.dtos.requests.CartItemsToAdd;
import org.codiz.onshop.dtos.requests.CartItemsUpdate;
import org.codiz.onshop.dtos.response.CartResponse;
import org.codiz.onshop.entities.cart.Cart;
import org.codiz.onshop.service.serv.cart.CartService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<Cart> addItemToCart(@RequestBody CartItemsToAdd itemsToAdd) {
            Cart cart = cartService.addItemToCart(itemsToAdd);
            return ResponseEntity.ok(cart);
    }

    @PutMapping("update-cart")
    public ResponseEntity<Cart> updateItemQuantity(@RequestBody CartItemsUpdate itemsUpdate){
        Cart cart = cartService.updateItemQuantity(itemsUpdate);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("remove-item")
    public ResponseEntity<Cart> removeItemFromCart(@RequestBody CartItemsDeletion deletion){
        Cart cart = cartService.removeItemFromCart(deletion);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCartById(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("productName").ascending());
        CartResponse cartResponse = cartService.getCartById(userId, pageable);
        return ResponseEntity.ok(cartResponse);
    }



}
