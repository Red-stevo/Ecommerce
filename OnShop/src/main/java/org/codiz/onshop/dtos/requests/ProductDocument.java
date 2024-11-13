package org.codiz.onshop.dtos.requests;

import io.vanslog.spring.data.meilisearch.annotations.Document;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import lombok.Data;

import java.util.List;


@Data
public class ProductDocument {
    private String productId;
    private String productName;
    private String productDescription;
    private String productCategory;
    private String productPrice;
    private List<String> productImageUrl;
}
