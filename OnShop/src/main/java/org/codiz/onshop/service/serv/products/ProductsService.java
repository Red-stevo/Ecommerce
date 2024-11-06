package org.codiz.onshop.service.serv.products;


import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsService {

    EntityCreationResponse postProductImage(List<ProductCreationRequest> requests);

}
