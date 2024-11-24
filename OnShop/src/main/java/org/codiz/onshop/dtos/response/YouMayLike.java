package org.codiz.onshop.dtos.response;

import lombok.Data;

@Data
public class YouMayLike {
    private String productId;
    private String productName;
    private String productImageUrl;
    private float productPrice;
    private int rating;
}
