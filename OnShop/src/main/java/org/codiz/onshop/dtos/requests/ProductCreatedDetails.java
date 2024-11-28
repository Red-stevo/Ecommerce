package org.codiz.onshop.dtos.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
public class ProductCreatedDetails {

    private float productPrice;

    private String color;

    private String size;

    private float discount;

    private List<MultipartFile> productUrls;

    private Integer count;


}
