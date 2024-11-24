package org.codiz.onshop.dtos.response;


import lombok.Data;

import java.io.Serializable;

@Data
public class ProductsPageResponse implements Serializable {
    private String productId;
    private String productName;
    private int ratings;
    private String productImagesUrl;
    private double discountedPrice;

}
