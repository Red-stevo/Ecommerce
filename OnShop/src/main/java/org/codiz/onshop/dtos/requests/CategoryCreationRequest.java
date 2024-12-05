package org.codiz.onshop.dtos.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryCreationRequest {
    private String categoryName;

}
