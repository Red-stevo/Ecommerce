package org.codiz.onshop.service.serv.products;


import org.codiz.onshop.dtos.requests.FileUploads;
import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.RatingsRequest;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.products.InventoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    String deleteProduct(String productId);
    void reduceProductQuantity(String productId, int quantity);
    void addProductQuantity(String productId, int quantity);
    EntityResponse createCategory(String categoryNames, MultipartFile uploads);
    EntityResponse updateCategory(String categoryId,String categoryName, MultipartFile fileUploads) throws IOException;
    List<CategoryResponse> findAllCategories();
    String deleteCategory(String categoryId) throws IOException;
    Page<InventoryResponse> inventoryList(InventoryStatus inventoryStatus, String categoryName,Float price1, Float price2, Pageable pageable );
    String addToWishList(String specificProductId, String userId);
    String deleteWishListItem(String userId, List<String> specificProductIds);
    List<WishListResponse> getWishList(String userId);
    List<DiscountedProductsResponse> findDiscountedProducts(int size);


}
