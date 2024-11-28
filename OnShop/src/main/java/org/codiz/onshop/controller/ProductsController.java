package org.codiz.onshop.controller;


import org.codiz.onshop.dtos.requests.CategoryCreationRequest;
import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.RatingsRequest;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.dtos.response.InventoryResponse;
import org.codiz.onshop.dtos.response.ProductsPageResponse;
import org.codiz.onshop.dtos.response.SpecificProductResponse;
import org.codiz.onshop.entities.products.Categories;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin( origins = "http://127.0.0.1:5173/", allowCredentials = "true")
public class ProductsController {
    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping(value = "/post")
    public ResponseEntity<EntityResponse> postProduct(
            @ModelAttribute ProductCreationRequest productCreationRequest) {

        return ResponseEntity.ok(productsService.postProduct(productCreationRequest));
    }


    @PostMapping("/rate")
    public ResponseEntity<EntityResponse> rateProduct(@RequestBody RatingsRequest request){
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
    public ResponseEntity<PagedModel<EntityModel<ProductsPageResponse>>> searchProducts(
            @RequestParam("query") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            PagedResourcesAssembler<ProductsPageResponse> assembler
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductsPageResponse> products = productsService.searchProducts(query, pageable);

        PagedModel<EntityModel<ProductsPageResponse>> pagedModel = assembler.toModel(products);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/get-products-page")
    public ResponseEntity<PagedModel<EntityModel<ProductsPageResponse>>> getProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            PagedResourcesAssembler<ProductsPageResponse> assembler) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductsPageResponse> productsPageResponse = productsService.productsPageResponseList(pageable);
        PagedModel<EntityModel<ProductsPageResponse>> pagedModel = assembler.toModel(productsPageResponse);
        return ResponseEntity.ok(pagedModel);
    }
    //SpecificProductResponse specificProductResponse(String productId,Pageable pageable)
    @GetMapping("/get/{productId}")
    public ResponseEntity<SpecificProductResponse> getProductById
            (@PathVariable String productId) {

        return ResponseEntity.ok(productsService.specificProductResponse(productId));
    }


    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryResponse>> showInventory(){
        return ResponseEntity.ok(productsService.showInventory());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Categories>> findAllCategories(){
        return ResponseEntity.ok(productsService.findAllCategories());
    }

    @GetMapping("/create-category")
    public ResponseEntity<EntityResponse> createCategory(List<CategoryCreationRequest> categoryCreationRequest){
        return ResponseEntity.ok(productsService.createCategory(categoryCreationRequest));
    }


}
