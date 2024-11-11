package org.codiz.onshop.controller;


import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.ProductDocument;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductsController {
    private final ProductsService productsService;

    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postProduct(
            @RequestPart("req") List<ProductCreationRequest> productCreationRequest) {
        productsService.postProduct(productCreationRequest);

        return ResponseEntity.ok("Product saved successfully");
    }



    @GetMapping("/search")
    public ResponseEntity<List<ProductDocument>> searchProducts(@RequestParam String query) {
        List<ProductDocument> products = productsService.searchProducts(query);
        return ResponseEntity.ok(products);
    }
}
