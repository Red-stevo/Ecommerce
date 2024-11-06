package org.codiz.onshop.service.impl.products;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.repositories.products.ProductsRepository;
import org.codiz.onshop.service.CloudinaryService;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {
    private final ProductsRepository productsRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Transactional
    public EntityCreationResponse postProductImage(List<ProductCreationRequest> requests){

        List<Products> products = requests.stream().map(
                request -> {

                    Products product = new Products();

                    try {
                        String url;
                        if (isImage((MultipartFile) request.getProductUrls())){
                            url = cloudinaryService.uploadImage((MultipartFile) request.getProductUrls());
                        } else if (isVideo((MultipartFile) request.getProductUrls())) {
                           url = cloudinaryService.uploadVideo((MultipartFile) request.getProductUrls());
                        }else
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

                        product.setProductName(request.getProductName());
                        product.setProductDescription(request.getProductDescription());
                        product.setProductPrice(request.getProductPrice());
                        product.setQuantity(request.getQuantity());
                        product.setProductImageUrl(url);

                        return product;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
        ).toList();

        productsRepository.saveAll(products);

        EntityCreationResponse res = new EntityCreationResponse();
        res.setMessage("created products successfully");
        res.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        res.setStatus(HttpStatus.OK);

        return res;
    }

    private boolean isVideo(MultipartFile file) {
        return file.getOriginalFilename().endsWith(".mp4");
    }

    private boolean isImage(MultipartFile file) {
        return file.getOriginalFilename().endsWith(".jpg");
    }


}
