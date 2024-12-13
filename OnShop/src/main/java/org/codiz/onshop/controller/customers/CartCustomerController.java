package org.codiz.onshop.controller.customers;

import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.CartItemsToAdd;
import org.codiz.onshop.dtos.requests.CartItemsUpdate;
import org.codiz.onshop.dtos.response.CartResponse;
import org.codiz.onshop.entities.cart.Cart;
import org.codiz.onshop.service.serv.cart.CartService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/cart")
public class CartCustomerController {

    private final CartService cartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity addItemToCart(@RequestBody CartItemsToAdd itemsToAdd) {

            return ResponseEntity.ok(cartService.addItemToCart(itemsToAdd));
    }

    @PutMapping("update-cart")
    public ResponseEntity<HttpStatus> updateItemQuantity(@RequestBody CartItemsUpdate itemsToAdd) {

        return ResponseEntity.ok( cartService.updateItemQuantity(itemsToAdd) );
    }

    @PutMapping("/remove-item")
    public ResponseEntity<HttpStatus> removeItemFromCart(@RequestBody List<String> cartItemIds){
        return ResponseEntity.ok(cartService.removeItemFromCart(cartItemIds));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCartById(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("productName").ascending());
        CartResponse cartResponse = cartService.getCartById(userId, pageable);
        return ResponseEntity.ok(cartResponse);
    }


    @DeleteMapping("/delete/cart")
    public ResponseEntity<HttpStatus> deleteCart(String cartId){
        return ResponseEntity.ok(cartService.deleteCart(cartId));
    }

}
