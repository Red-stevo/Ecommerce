package org.codiz.onshop.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class ProductUpdateRequest {
    private String productId;

    private List<String> categoryName;

    private String productName;

    private String productDescription;

    private int inventoryStatus;

    private SpecificProductUpdate productUpdates;


}
