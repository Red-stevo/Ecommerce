package org.codiz.onshop.service.serv.cart;

import org.codiz.onshop.dtos.response.EntityResponse;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    EntityResponse createCart(String userId);

}
