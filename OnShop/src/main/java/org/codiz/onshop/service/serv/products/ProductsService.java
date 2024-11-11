package org.codiz.onshop.service.serv.products;


import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.ProductDocument;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductsService {

    EntityCreationResponse postProduct(List<ProductCreationRequest> requests);
    List<ProductDocument> searchProducts(String query);

}
