package org.codiz.onshop.service.serv.products.cart;

import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    EntityCreationResponse createCart(String userId);

}
