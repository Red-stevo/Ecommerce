package org.codiz.onshop.controller;

import lombok.RequiredArgsConstructor;
import org.codiz.onshop.service.serv.cart.CartService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

}
