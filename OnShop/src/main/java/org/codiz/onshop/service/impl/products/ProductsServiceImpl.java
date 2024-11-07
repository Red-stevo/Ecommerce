package org.codiz.onshop.service.impl.products;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.ProductUrls;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.codiz.onshop.entities.products.ProductImages;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.repositories.products.ProductImagesRepository;
import org.codiz.onshop.repositories.products.ProductsRepository;
import org.codiz.onshop.service.CloudinaryService;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {
    private final ProductsRepository productsRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final ProductImagesRepository productImagesRepository;

    @Transactional
    public EntityCreationResponse postProductImage(List<ProductCreationRequest> requests) throws IOException {

        List<Products> products = requests.stream().map(request -> {
            Products product = new Products();
            product.setProductName(request.getProductName());
            product.setProductDescription(request.getProductDescription());
            product.setProductPrice(request.getProductPrice());
            product.setQuantity(request.getQuantity());

            // Process and set images for each product
            List<ProductImages> productImages = null;
            try {
                productImages = setImageUrls(request.getProductUrls());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            product.setProductImages(productImages);

            return product;
        }).toList();

        productsRepository.saveAll(products);

        // Response setup
        EntityCreationResponse res = new EntityCreationResponse();
        res.setMessage("Created products successfully");
        res.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        res.setStatus(HttpStatus.OK);

        return res;
    }




    @Transactional
    public List<ProductImages> setImageUrls(List<ProductUrls> images) throws IOException {
        List<ProductImages> imagesList = new ArrayList<>();
        for (ProductUrls image : images) {
            ProductImages productImage = new ProductImages();
            String url;

            // Determine if the file is an image or video
            if (isImage((MultipartFile) image)) {
                url = cloudinaryService.uploadImage((MultipartFile) image);
            } else if (isVideo((MultipartFile) image)) {
                url = cloudinaryService.uploadVideo((MultipartFile) image);
            } else {
                continue; // Skip unsupported file types
            }

            productImage.setImageUrl(url);
            imagesList.add(productImage);
        }

        // Save all images and return the list
        productImagesRepository.saveAll(imagesList);
        return imagesList;
    }



    private boolean isVideo(MultipartFile file) {
        return file.getOriginalFilename().endsWith(".mp4");
    }

    private boolean isImage(MultipartFile file) {
        return file.getOriginalFilename().endsWith(".jpg");
    }


}
