package org.codiz.onshop.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class SpecificProductResponse {

    private String productName;
    private double productPrice;
    private String productDescription;
    private List<SpecificProductDetails> products;
    private List<RelatedProducts> relatedProducts;
    private List<ProductReviews> reviews;
}
