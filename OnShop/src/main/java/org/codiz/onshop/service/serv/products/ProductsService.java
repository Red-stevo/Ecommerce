package org.codiz.onshop.service.serv.products;


import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.ProductDocument;
import org.codiz.onshop.entities.products.ProductImages;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductsService {

    void postProduct(List<ProductCreationRequest> requests);
    List<ProductImages> setImageUrls(List<String> images) throws IOException ;
    List<ProductDocument> searchProducts(String query);

}
