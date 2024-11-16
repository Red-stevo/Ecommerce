package org.codiz.onshop.dtos.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SpecificProductResponse {

    private String productName;
    private double productPrice;
    private String productDescription;
    private List<String> productImageUrl;
    private double productRating;
    private String color;
    private String brand;
    private String aboutProduct;
    private Map<Integer,Double> percentageRating;
    private double discountedPrice;
}
