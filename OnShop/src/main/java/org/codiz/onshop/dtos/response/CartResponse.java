package org.codiz.onshop.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class CartResponse {

    private String username;

    private String cartId;

    private List<CartItemsResponse> cartItemsResponses;

    private List<YouMayLike> youMayLikes;

    private int currentPage;

    private int totalPages;

    private boolean hasMore;
}
