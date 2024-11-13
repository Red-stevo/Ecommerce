package org.codiz.onshop.service.serv.cart;

import org.codiz.onshop.dtos.requests.CartItemsToAdd;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.entities.products.Products;
import org.springframework.stereotype.Service;

@Service
public interface CartsItemsService {
    EntityResponse addItemToCart(CartItemsToAdd cartItemsToAdd);
    EntityResponse removeItemFromCart(Products products);
    EntityResponse updateCartItem(Integer quantity,String cartItemId);
}
