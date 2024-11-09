package org.codiz.onshop.service.serv.cart;

import org.codiz.onshop.dtos.requests.CartItemsToAdd;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.springframework.stereotype.Service;

@Service
public interface CartsItemsService {
    EntityCreationResponse addItemToCart(CartItemsToAdd cartItemsToAdd);
}
