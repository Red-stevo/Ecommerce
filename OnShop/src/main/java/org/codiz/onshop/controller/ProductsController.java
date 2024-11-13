package org.codiz.onshop.controller;


import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.ProductDocument;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductsController {
    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping(value = "/post")
    public ResponseEntity<?> postProduct(
            @ModelAttribute List<ProductCreationRequest> productCreationRequest) {
        productsService.postProduct(productCreationRequest);

        return ResponseEntity.ok("Product saved successfully");
    }



    @GetMapping("/search")
    public ResponseEntity<List<ProductDocument>> searchProducts(@RequestParam String query) {
        List<ProductDocument> products = productsService.searchProducts(query);
        return ResponseEntity.ok(products);
    }
}
