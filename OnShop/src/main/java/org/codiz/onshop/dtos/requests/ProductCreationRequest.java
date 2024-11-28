package org.codiz.onshop.dtos.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Data
public class ProductCreationRequest {
    private List<String> categoryName;
    private String productName;

    private String productDescription;

    private List<ProductCreatedDetails> productCreatedDetails;


}
