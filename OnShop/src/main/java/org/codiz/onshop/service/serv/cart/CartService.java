package org.codiz.onshop.service.serv.cart;

import org.codiz.onshop.dtos.requests.CartItemsDeletion;
import org.codiz.onshop.dtos.requests.CartItemsToAdd;
import org.codiz.onshop.dtos.requests.CartItemsUpdate;
import org.codiz.onshop.entities.cart.Cart;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CartService {


    Optional<Cart> getCartById(String cartId);
    Cart removeItemFromCart(CartItemsDeletion deletion);
    Cart updateItemQuantity(CartItemsUpdate itemsUpdate);
    Cart addItemToCart(CartItemsToAdd items);

}
