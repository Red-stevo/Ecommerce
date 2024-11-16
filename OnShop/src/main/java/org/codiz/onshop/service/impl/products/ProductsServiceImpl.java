package org.codiz.onshop.service.impl.products;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.RatingsRequest;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.dtos.response.ProductsPageResponse;
import org.codiz.onshop.dtos.response.SpecificProductResponse;
import org.codiz.onshop.entities.products.Categories;
import org.codiz.onshop.entities.products.ProductImages;
import org.codiz.onshop.entities.products.ProductRatings;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.products.CategoriesRepository;
import org.codiz.onshop.repositories.products.ProductImagesRepository;
import org.codiz.onshop.repositories.products.ProductRatingsRepository;
import org.codiz.onshop.repositories.products.ProductsJpaRepository;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.codiz.onshop.service.CloudinaryService;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {
    private final ProductsJpaRepository productsRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;
    private final ProductImagesRepository productImagesRepository;
    private final CategoriesRepository categoriesRepository;
    private final UsersRepository usersRepository;
    private final ProductRatingsRepository ratingsRepository;

    @Transactional
    @Cacheable(value = "products")
    public EntityResponse postProduct(List<ProductCreationRequest> requests) {
        try {
            List<Products> products = requests.stream().map(request -> {
                Products product = new Products();


                settingProduct(request, product);

                // Set product images
                List<ProductImages> images = setImageUrls(request.getProductUrls());
                product.setProductImages(images); // Link images to the product

                List<Categories> categories= settingCategory(request);
                product.setCategories(categories);
                categories.forEach(categories1 -> categories1.getProducts().add(product));

                return product;
            }).toList();

            // Save products with images
            products.forEach(product -> {
                productsRepository.save(product); // Cascade saves product images if configured correctly
                product.getProductImages().forEach(image -> image.getProducts().add(product)); // Maintain bidirectional relationship
            });



            EntityResponse response = new EntityResponse();
            response.setMessage("Successfully posted the product");
            response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            response.setStatus(HttpStatus.OK);

            return response;

        } catch (Exception e) {
            log.error("Error during product creation: " + e.getMessage(), e);
            throw new RuntimeException("Error during product creation", e);
        }
    }


    @Cacheable(value = "products", key = "#query + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<Products> searchProducts(String query, Pageable pageable) {
        Page<Products> productPage = productsRepository
                .findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(query, query, pageable);

        Page<Categories> categoryPage = categoriesRepository.findCategoriesByCategoryNameIgnoreCase(query, pageable);

        List<Products> categoryProducts = categoryPage.getContent().stream()
                .flatMap(category -> category.getProducts().stream())
                .toList();

        List<Products> combinedResults = Stream.concat(
                productPage.getContent().stream(),
                categoryProducts.stream()
        ).distinct().collect(Collectors.toList());


        return new PageImpl<>(combinedResults, pageable, combinedResults.size());
    }




    /*
    * Method to rate products
    * */


    public EntityResponse addRating(RatingsRequest rating) {
        Products product = productsRepository.findById(rating.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Users users = usersRepository.findUsersByUserId(rating.getUserId());

        ProductRatings productRating = new ProductRatings();
        productRating.setProduct(product);
        productRating.setUser(users);
        productRating.setRating(rating.getRating());
        productRating.setReview(rating.getComment());

        ratingsRepository.save(productRating);

        EntityResponse response = new EntityResponse();
        response.setMessage("Successfully rated the product");
        response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.OK);

        return response;
    }



/*
* Method to get products for the products page
* */


    public List<ProductsPageResponse> productsPageResponseList(Pageable pageable) {
        List<Products> products = productsRepository.findAll(pageable).getContent();

        List<ProductsPageResponse> productsPageResponses = new ArrayList<>();

        for (Products product : products) {
            ProductsPageResponse response = new ProductsPageResponse();
            response.setProductName(product.getProductName());
            response.setProductId(product.getProductId());

            if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
                response.setProductImagesUrls(product.getProductImages().get(0).getImageUrl());
            }

            response.setRatings(getAverageRating(product.getProductId()));
            productsPageResponses.add(response);
        }
        return productsPageResponses;
    }



    /*
    * method to return details of a specific product
    *
    * */


    public SpecificProductResponse specificProductResponse(String productId){

        Products products = productsRepository.findProductsByProductId(productId).orElseThrow(
                ()->new RuntimeException("the product does not exist")
        );

        SpecificProductResponse specificProductResponse = new SpecificProductResponse();
        specificProductResponse.setProductName(products.getProductName());
        specificProductResponse.setProductDescription(products.getProductDescription());
        specificProductResponse.setProductPrice(products.getProductPrice());
        specificProductResponse.setColor(products.getColor());
        specificProductResponse.setBrand(products.getBrand());
        specificProductResponse.setAboutProduct(products.getAboutProduct());
        ;
        List<String> imageUrls = products.getProductImages().stream()
                .map(ProductImages::getImageUrl)
                .toList();
        specificProductResponse.setProductImageUrl(imageUrls);


        double averageRating = products.getRatings().stream()
                .mapToDouble(ProductRatings::getRating)
                .average().orElse(0.0);
        specificProductResponse.setProductRating(averageRating);

        Map<Integer,Long> ratingCounts = products.getRatings().stream()
                .collect(Collectors.groupingBy(ProductRatings::getRating, Collectors.counting()));

        int totalRatings = ratingCounts.size();
        Map<Integer,Double> percentageRatings = ratingCounts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,entry->((double)entry.getValue() * 100.0) / totalRatings));

        specificProductResponse.setPercentageRating(percentageRatings);



        return specificProductResponse;
    }



    /*
    * Method to update the products
    * */

    @Transactional
    @CacheEvict(value = "products", key = "#productId")
    public EntityResponse updateProduct(String productId, ProductCreationRequest updateRequest) {
        try {

            Products existingProduct = productsRepository.findProductsByProductId(productId).orElseThrow(
                    () -> new RuntimeException("The product with ID " + productId + " does not exist")
            );

            // Update product details
            settingProduct(updateRequest, existingProduct);

            // Update product images
            if (updateRequest.getProductUrls() != null && !updateRequest.getProductUrls().isEmpty()) {
                // Clear existing images
                for (ProductImages productImages:existingProduct.getProductImages()) {
                    String url = productImages.getImageUrl();
                    cloudinaryService.deleteImage(url);
                }
                existingProduct.getProductImages().forEach(image -> image.getProducts().remove(existingProduct));
                existingProduct.getProductImages().clear();

                // Add new images
                List<ProductImages> updatedImages = setImageUrls(updateRequest.getProductUrls());
                updatedImages.forEach(image -> image.getProducts().add(existingProduct));
                existingProduct.setProductImages(updatedImages);
            }

            // Update categories
            if (updateRequest.getCategoryCreationRequestList() != null) {
                //settingCategory(updateRequest);
                List<Categories> updatedCategories = settingCategory(updateRequest);

                // Clear existing categories
                existingProduct.getCategories().forEach(category -> category.getProducts().remove(existingProduct));
                existingProduct.getCategories().clear();

                // Add new categories
                updatedCategories.forEach(category -> category.getProducts().add(existingProduct));
                existingProduct.setCategories(updatedCategories);
            }

            // Save the updated product
            productsRepository.save(existingProduct);

            // Response
            EntityResponse response = new EntityResponse();
            response.setMessage("Successfully updated the product");
            response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            response.setStatus(HttpStatus.OK);

            return response;

        } catch (Exception e) {
            log.error("Error during product update: " + e.getMessage(), e);
            throw new RuntimeException("Error during product update", e);
        }
    }



    /*
    * method to delete a product
    * */

    public String deleteProduct(String productId){
        Products products = productsRepository.findProductsByProductId(productId).orElseThrow(
                ()->new RuntimeException("The product with ID " + productId + " does not exist")
        );


        for (ProductImages productImages:products.getProductImages()) {
            try {
                String url = productImages.getImageUrl();
                cloudinaryService.deleteImage(url);
            }catch (IOException e){
                throw new RuntimeException("could not delete image from cloudinary");
            }

        }

        productsRepository.delete(products);
        return "product successfully deleted";

    }

    private List<Categories> settingCategory(ProductCreationRequest updateRequest) {
        // Upload category icon if provided
        return updateRequest.getCategoryCreationRequestList().stream()
                .map(request -> {
                    Optional<Categories> existingCategory = categoriesRepository
                            .findCategoriesByCategoryNameIgnoreCase(request.getCategoryName());

                    return existingCategory.orElseGet(() -> {
                        Categories newCategory = new Categories();
                        newCategory.setCategoryName(request.getCategoryName());

                        // Upload category icon if provided
                        if (request.getCategoryIcon() != null) {
                            String iconUrl = uploadIconToCloudinary(request.getCategoryIcon());
                            newCategory.setCategoryIcon(iconUrl);
                        }

                        return categoriesRepository.save(newCategory);
                    });
                }).toList();
    }

    private void settingProduct(ProductCreationRequest updateRequest, Products existingProduct) {
        existingProduct.setProductName(updateRequest.getProductName());
        existingProduct.setProductDescription(updateRequest.getProductDescription());
        existingProduct.setProductPrice(updateRequest.getProductPrice());
        existingProduct.setQuantity(updateRequest.getQuantity());
        existingProduct.setAboutProduct(updateRequest.getAboutProduct());
        existingProduct.setBrand(updateRequest.getBrand());
        existingProduct.setColor(updateRequest.getColor());
    }


    public int getAverageRating(String productId) {

        Double averageRating = ratingsRepository.findAverageRatingByProductId(productId);
        return averageRating != null ? (int) Math.round(averageRating) : 0;
    }

    @NotNull
    private List<ProductImages> setImageUrls(List<MultipartFile> files) {
        List<ProductImages> productImages = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                ProductImages productImage = getProductImage(file);
                if (productImage != null) {
                    productImages.add(productImage);
                    productImagesRepository.save(productImage);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error uploading file: ");
            }
        }
        return productImages;
    }


    private ProductImages getProductImage(MultipartFile file) throws IOException {

        ProductImages productImage = new ProductImages();
        String url;

        if (file != null) {
            if (isImage(file)) {
                url = cloudinaryService.uploadImage((MultipartFile) file);
                productImage.setImageUrl(url);
            } else if (isVideo(file)) {
                url = cloudinaryService.uploadVideo((MultipartFile) file);
                productImage.setImageUrl(url);
            } else {
                log.warn("Unsupported file type for image/video upload: ");
                return null;
            }
            return productImage;
        }else {
            return null;
        }


    }

    private boolean isVideo(MultipartFile fileStream) {
        return Objects.requireNonNull(fileStream.getOriginalFilename()).endsWith(".mp4");
    }


    private boolean isImage(MultipartFile fileStream) {
        return Objects.requireNonNull(fileStream.getOriginalFilename()).toLowerCase().endsWith(".jpg")
                || Objects.requireNonNull(fileStream.getOriginalFilename()).toLowerCase().endsWith(".png")
                || Objects.requireNonNull(fileStream.getOriginalFilename()).toLowerCase().endsWith(".gif")
                || Objects.requireNonNull(fileStream.getOriginalFilename()).toLowerCase().endsWith(".jpeg");
    }






    private String uploadIconToCloudinary(MultipartFile iconImage) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(iconImage, ObjectUtils.emptyMap());
            return (String) uploadResult.get("url"); // Extract and return the URL
        } catch (IOException e) {
            log.error("Error uploading icon to Cloudinary: " + e.getMessage(), e);
            throw new RuntimeException("Failed to upload category icon", e);
        }
    }





}
