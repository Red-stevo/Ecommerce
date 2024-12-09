package org.codiz.onshop.controller.customers;


import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.RatingsRequest;
import org.codiz.onshop.dtos.response.*;
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

@Slf4j
@RestController
@RequestMapping("/api/v1//customer/products")
//@CrossOrigin( origins = "http://127.0.0.1:5173/", allowCredentials = "true")
public class ProductsCustomerController {
    private final ProductsService productsService;

    public ProductsCustomerController(ProductsService productsService) {
        this.productsService = productsService;
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


    //SpecificProductResponse specificProductResponse(String productId,Pageable pageable)
    /*@GetMapping("/get/{productId}")
    public ResponseEntity<SpecificProductResponse> getProductById
            (@PathVariable String productId) {

        return ResponseEntity.ok(productsService.specificProductResponse(productId));
    }*/



    /*@GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> findAllCategories(){
        return ResponseEntity.ok(productsService.findAllCategories());
    }*/



    @DeleteMapping("/delete-wishlist")
    public ResponseEntity<String> deleteWishListItem(@RequestParam String userId, @RequestParam List<String> specificProductIds){
        return ResponseEntity.ok().body(productsService.deleteWishListItem(userId,specificProductIds));
    }

    @PostMapping("/add-to-wishlist")
    public ResponseEntity<String> addToWishList(@RequestParam String specificProductId, @RequestParam String userId){
        return ResponseEntity.ok().body(productsService.addToWishList(specificProductId,userId));
    }

    @GetMapping("/show-wishlist")
    public ResponseEntity<List<WishListResponse>> getWishList(String userId){
        return ResponseEntity.ok(productsService.getWishList(userId));
    }
}
