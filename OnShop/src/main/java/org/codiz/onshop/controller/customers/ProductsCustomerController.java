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
@RequestMapping("/api/v1/customer/products")
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



    @DeleteMapping("/delete-wishlist/{userId}")
    public ResponseEntity<String> deleteWishListItem(@RequestBody String userId,@RequestBody(required = false) String specificProductIds){
        return ResponseEntity.ok().body(productsService.deleteWishListItem(userId,specificProductIds));
    }

    @PostMapping("/add-to-wishlist")
    public ResponseEntity addToWishList(@RequestParam String specificProductId, @RequestParam String userId){
        return ResponseEntity.ok().body(productsService.addToWishList(specificProductId,userId));
    }

    @GetMapping("/show-wishlist/{userId}")
    public ResponseEntity<List<WishListResponse>> getWishList(@PathVariable String userId){
        return ResponseEntity.ok(productsService.getWishList(userId));
    }


}
