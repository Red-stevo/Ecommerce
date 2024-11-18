package org.codiz.onshop.dtos.requests;

import lombok.Data;
import org.codiz.onshop.entities.products.Categories;

import java.util.List;


@Data
public class ProductDocument {
    private String productId;
    private String productName;
    private String productDescription;
    private List<Categories> productCategory;
    private String productPrice;
    private List<String> productImageUrl;
}
