package org.codiz.onshop.service.serv.cart;

import org.codiz.onshop.dtos.requests.CartItemsToAdd;
import org.codiz.onshop.dtos.requests.CartItemsUpdate;
import org.codiz.onshop.dtos.response.CartResponse;
import org.codiz.onshop.entities.cart.Cart;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {

    HttpStatus deleteCart(String cartId);
    CartResponse getCartById(String userId, Pageable pageable);
    HttpStatus removeItemFromCart(List<String> cartItemId);
    HttpStatus updateItemQuantity(String cartId, Integer quantity);
    ResponseEntity addItemToCart(CartItemsToAdd items);

}
