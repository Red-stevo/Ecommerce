package org.codiz.onshop.controller.open;

import lombok.AllArgsConstructor;
import org.codiz.onshop.dtos.response.CategoryResponse;
import org.codiz.onshop.dtos.response.DiscountedProductsResponse;
import org.codiz.onshop.dtos.response.ProductsPageResponse;
import org.codiz.onshop.dtos.response.SpecificProductResponse;
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
@AllArgsConstructor
@RequestMapping("api/v1/open/products")
@CrossOrigin(origins = "http://192.168.100.27:5173/", allowCredentials = "true")
public class OpenProductsController {
    private final ProductsService productsService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> findAllCategories(){
        return ResponseEntity.ok(productsService.findAllCategories());
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

    @GetMapping("/get/{productId}")
    public ResponseEntity<SpecificProductResponse> getProductById
            (@PathVariable String productId) {

        return ResponseEntity.ok(productsService.specificProductResponse(productId));
    }

    @GetMapping("/discounted")
    public ResponseEntity<List<DiscountedProductsResponse>> findDiscountedProducts(@RequestParam int size){
        return ResponseEntity.ok(productsService.findDiscountedProducts(size));
    }

    @GetMapping("/popular-products")
    public ResponseEntity<List<ProductsPageResponse>> popularProducts(@RequestParam int size){
        return ResponseEntity.ok(productsService.popularProducts(size));
    }

    @GetMapping("/new-products")
    public ResponseEntity<List<ProductsPageResponse>> newProducts(@RequestParam int size){
        return ResponseEntity.ok(productsService.newProducts(size));
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

}
