package org.codiz.onshop.controller;


import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductsController {
    private final ProductsService productsService;

    @PostMapping
    public ResponseEntity<EntityCreationResponse> postProduct(@RequestBody List<ProductCreationRequest> req){
        EntityCreationResponse res = productsService.postProductImage(req);
        return ResponseEntity.ok(res);
    }
}
