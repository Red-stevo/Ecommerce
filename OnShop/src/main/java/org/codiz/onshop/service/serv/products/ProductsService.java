package org.codiz.onshop.service.serv.products;


import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.RatingsRequest;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.dtos.response.InventoryResponse;
import org.codiz.onshop.dtos.response.ProductsPageResponse;
import org.codiz.onshop.dtos.response.SpecificProductResponse;
import org.codiz.onshop.entities.products.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsService {

    EntityResponse postProduct(List<ProductCreationRequest> requests);
    Page<Products> searchProducts(String query, Pageable pageable);
    EntityResponse addRating(RatingsRequest rating);
    List<ProductsPageResponse> productsPageResponseList(Pageable pageable);
    SpecificProductResponse specificProductResponse(String productId);
    EntityResponse updateProduct(String productId, ProductCreationRequest updateRequest);
    String deleteProduct(String productId);
    InventoryResponse showProductInventory(String productId);
    List<InventoryResponse> showInventory(Pageable pageable);
    void reduceProductQuantity(String productId, int quantity);
    void addProductQuantity(String productId, int quantity);


}
