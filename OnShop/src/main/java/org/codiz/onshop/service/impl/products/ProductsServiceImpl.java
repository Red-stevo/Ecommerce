package org.codiz.onshop.service.impl.products;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.ProductDocument;
import org.codiz.onshop.entities.products.ProductImages;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.repositories.products.ProductImagesRepository;
import org.codiz.onshop.repositories.products.ProductSearchRepository;
import org.codiz.onshop.repositories.products.ProductsJpaRepository;
import org.codiz.onshop.service.CloudinaryService;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {
    private final ProductsJpaRepository productsRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final ProductImagesRepository productImagesRepository;
    private final ProductSearchRepository searchRepository;

    @Transactional
    public void postProduct(List<ProductCreationRequest> requests) {
        try {
            List<Products> products = requests.stream().map(request -> {
                Products product = new Products();
                product.setProductName(request.getProductName());
                product.setProductDescription(request.getProductDescription());
                product.setProductPrice(request.getProductPrice());
                product.setQuantity(request.getQuantity());

                // Set product images
                List<ProductImages> images = setImageUrls(request.getProductUrls());
                product.setProductImages(images); // Link images to the product

                return product;
            }).toList();

            // Save products with images
            productsRepository.saveAll(products);

            // Index products in MeiliSearch
            List<ProductDocument> productsSearchList = products.stream()
                    .map(product -> {
                        ProductDocument searchProduct = modelMapper.map(product, ProductDocument.class);
                        searchProduct.setProductImageUrl(product.getProductImages().stream()
                                .map(ProductImages::getImageUrl)
                                .toList());
                        return searchProduct;
                    })
                    .toList();

            searchRepository.saveAll(productsSearchList);

            /*// Create response
            EntityCreationResponse res = new EntityCreationResponse();
            res.setMessage("Created products successfully");
            res.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            res.setStatus(HttpStatus.OK);

            log.info("Successfully created and indexed products.");
            return res;*/

        } catch (Exception e) {
            log.error("Error during product creation: " + e.getMessage(), e);
            throw new RuntimeException("Error during product creation", e);
        }
    }

    @Transactional
    public List<ProductImages> setImageUrls(List<String> images) {
        List<ProductImages> imagesList = new ArrayList<>();
        for (String image : images) {
            ProductImages productImage = getProductImages(image);
            imagesList.add(productImage);

            /*} catch (IOException e) {
                log.error("Error uploading media file: " + e.getMessage(), e);
                throw new RuntimeException("Error uploading media file", e);
            }*/
        }

        // Save images to repository and return the list
        return productImagesRepository.saveAll(imagesList);
    }

    @NotNull
    private static ProductImages getProductImages(String image) {
        ProductImages productImage = new ProductImages();
        //String url;

        /*try {
            *//*if (isImage(image)) {*//*
                url = cloudinaryService.uploadImage(image);
            } else if (isVideo(image)) {
                url = cloudinaryService.uploadVideo(image);
            } else {
                log.warn("Unsupported file type for image/video upload.");
                continue;
            }*/
        productImage.setImageUrl(image);
        return productImage;
    }


    public List<ProductDocument> searchProducts(String query) {

        return searchRepository.search(query);

    }






    private boolean isVideo(MultipartFile file) {
        return file.getOriginalFilename().endsWith(".mp4");
    }

    private boolean isImage(MultipartFile file) {
        return file.getOriginalFilename().endsWith(".jpg");
    }


}
