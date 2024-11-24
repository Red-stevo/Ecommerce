package org.codiz.onshop.dtos.response;

import lombok.Data;

@Data
public class ProductReviews {
    private String username;
    private String reviewContent;
    private int rating;
}
