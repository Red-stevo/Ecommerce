package org.codiz.onshop.service.serv.cart;

import org.codiz.onshop.dtos.requests.CartCreationRequest;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.entities.cart.Cart;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    Cart createCart(Cart cart);

}
