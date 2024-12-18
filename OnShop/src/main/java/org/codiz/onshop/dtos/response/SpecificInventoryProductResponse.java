package org.codiz.onshop.dtos.response;

import lombok.Data;
import org.codiz.onshop.entities.products.SpecificProductDetails;

import java.util.List;

@Data
public class SpecificInventoryProductResponse {
    private String productId;
    private String productName;
    private String productDescription;
    private List<String> productCategory;
    private List<SpecificProductDetailsResponse> specificProducts;
}
