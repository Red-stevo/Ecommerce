package org.codiz.onshop.dtos.requests;

import lombok.Data;
import org.codiz.onshop.entities.products.Categories;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
public class ProductUpdate {
    private int count;
    private String color;
    private List<MultipartFile> images;
    private List<Categories> categories;
}
