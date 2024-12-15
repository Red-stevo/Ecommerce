package org.codiz.onshop.dtos.response;

import lombok.Data;

@Data
public class SpecificOrderItemResponse {
    private String productName;
    private String productImageUrl;
    private int quantity;
    private String variety;
    private String proportion;
    private boolean isActive;
    private String productDescription;


}
