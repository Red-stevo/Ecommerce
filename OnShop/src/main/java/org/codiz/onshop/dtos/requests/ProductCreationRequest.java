package org.codiz.onshop.dtos.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Data
public class ProductCreationRequest {

    private String productName;

    private String productDescription;

    private float productPrice;

    private String brand;

    private String color;

    private String aboutProduct;

    private double discount;

    /*private List<String> productUrls;*/
    private List<MultipartFile> productUrls;

    private Integer quantity;

    private List<CategoryCreationRequest> categoryCreationRequestList;

}
