package org.codiz.onshop.service.serv.products;


import org.codiz.onshop.dtos.requests.*;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.products.InventoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface ProductsService {

    CompletableFuture<EntityResponse> postProduct(ProductCreationRequest requests, List<FileUploads> uploads);
    Page<ProductsPageResponse> searchProducts(String query, Pageable pageable);
    EntityResponse addRating(RatingsRequest rating);
    Page<ProductsPageResponse> productsPageResponseList(Pageable pageable);
    SpecificProductResponse specificProductResponse(String productId);
    EntityResponse updateProduct(String productId, ProductCreationRequest updateRequest,List<FileUploads> uploads);
    HttpStatus deleteProduct(String productId);
    void reduceProductQuantity(String productId, int quantity);
    void addProductQuantity(String productId, int quantity);
    EntityResponse createCategory(String categoryNames, MultipartFile uploads);
    EntityResponse updateCategory(String categoryId,String categoryName, MultipartFile fileUploads) throws IOException;
    List<CategoryResponse> findAllCategories();
    String deleteCategory(String categoryId) throws IOException;
    Page<InventoryResponse> inventoryList( Pageable pageable );
    ResponseEntity addToWishList(String specificProductId, String userId);
    HttpStatus deleteWishListItem(String userId, String specificProductIds);
    List<WishListResponse> getWishList(String userId);
    List<DiscountedProductsResponse> findDiscountedProducts(int size);
    HttpStatus deleteProductImage(String image);
    SpecificInventoryProductResponse getInventoryProduct(String specificProductId);
    HttpStatus updateProduct(ProductUpdateRequest request,List<MultipartFile> uploads);



}
