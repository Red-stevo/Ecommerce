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

    private String color;

    private double discount;

    private List<MultipartFile> productUrls;

    private Integer count;

    private List<String> categoryIds;


}
