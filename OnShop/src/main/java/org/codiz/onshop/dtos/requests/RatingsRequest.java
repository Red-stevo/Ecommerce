package org.codiz.onshop.dtos.requests;

import lombok.Data;

@Data
public class RatingsRequest {

    private String productId;
    private int rating;
    private String comment;
    private String userId;

}
