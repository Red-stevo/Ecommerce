package org.codiz.onshop.service.serv.products;


import org.codiz.onshop.dtos.requests.CategoryCreationRequest;
import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.RatingsRequest;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.dtos.response.InventoryResponse;
import org.codiz.onshop.dtos.response.ProductsPageResponse;
import org.codiz.onshop.dtos.response.SpecificProductResponse;
import org.codiz.onshop.entities.products.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsService {

    EntityResponse postProduct(ProductCreationRequest requests);
    Page<ProductsPageResponse> searchProducts(String query, Pageable pageable);
    EntityResponse addRating(RatingsRequest rating);
    Page<ProductsPageResponse> productsPageResponseList(Pageable pageable);
    SpecificProductResponse specificProductResponse(String productId);
    EntityResponse updateProduct(String productId, ProductCreationRequest updateRequest);
    String deleteProduct(String productId);
    InventoryResponse showProductInventory(String productId);
    List<InventoryResponse> showInventory();
    void reduceProductQuantity(String productId, int quantity);
    void addProductQuantity(String productId, int quantity);
    EntityResponse createCategory(List<CategoryCreationRequest> categoryCreationRequest);
    EntityResponse updateCategory(String categoryId,CategoryCreationRequest categoryCreationRequest);
    List<Categories> findAllCategories();
    String deleteCategory(String categoryId);


}
