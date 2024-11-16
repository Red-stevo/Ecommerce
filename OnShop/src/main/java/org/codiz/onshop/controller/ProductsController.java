package org.codiz.onshop.controller;


import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.ProductDocument;
import org.codiz.onshop.dtos.requests.RatingsRequest;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
    public ResponseEntity<EntityResponse> postProduct(
            @ModelAttribute List<ProductCreationRequest> productCreationRequest) {

        return ResponseEntity.ok(productsService.postProduct(productCreationRequest));
    }


    @PostMapping("/rate")
    public ResponseEntity<EntityResponse> rateProduct(RatingsRequest request){
        EntityResponse rate = productsService.addRating(request);
        return ResponseEntity.ok(rate);
    }


    /**
     * Endpoint to search for products.
     * @param query The search query string.
     * @param page The page number for pagination.
     * @param size The page size for pagination.
     * @return A paginated list of products matching the search criteria.
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam("query") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            PagedResourcesAssembler<Products> assembler
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Products> products = productsService.searchProducts(query, pageable);

        PagedModel<?> pagedModel = assembler.toModel(products);
        return ResponseEntity.ok(pagedModel);
    }
}
