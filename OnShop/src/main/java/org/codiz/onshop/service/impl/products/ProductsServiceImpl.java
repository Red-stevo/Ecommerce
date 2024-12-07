package org.codiz.onshop.service.impl.products;

import com.cloudinary.Cloudinary;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.*;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.products.*;
import org.codiz.onshop.entities.users.UserProfiles;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.products.*;
import org.codiz.onshop.repositories.users.UserProfilesRepository;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {
    private final ProductsJpaRepository productsRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;
    private final CategoriesRepository categoriesRepository;
    private final UsersRepository usersRepository;
    private final ProductRatingsRepository ratingsRepository;
    private final InventoryRepository inventoryRepository;
    private final SpecificProductsRepository specificProductsRepository;
    private final UserProfilesRepository userProfilesRepository;


    //UserProfiles userProfiles;


    @Transactional
    public EntityResponse createCategory(String categoryNames, MultipartFile uploads) {

        try {
            FileUploads fileUploads = new FileUploads(
                    uploads.getOriginalFilename(),uploads.getBytes()
            );

            Categories categories = new Categories();
            String url = cloudinaryService.uploadImage(fileUploads);
            categories.setCategoryName(categoryNames);
            categories.setCategoryIcon(url);
            categoriesRepository.save(categories);

            EntityResponse entityResponse = new EntityResponse();
            entityResponse.setMessage("Category created successfully");
            entityResponse.setCreatedAt(Timestamp.from(Instant.now()));
            entityResponse.setStatus(HttpStatus.OK);
            return entityResponse;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    @Transactional
    public EntityResponse updateCategory(String categoryId,String categoryName, FileUploads fileUploads) {
        Categories categories = categoriesRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        categories.setCategoryName(categoryName);
        if (fileUploads != null) {
            String url = cloudinaryService.uploadImage(fileUploads);
            categories.setCategoryIcon(url);
        }
        categoriesRepository.save(categories);
        EntityResponse entityResponse = new EntityResponse();
        entityResponse.setMessage("Category updated successfully");
        entityResponse.setCreatedAt(Timestamp.from(Instant.now()));
        entityResponse.setStatus(HttpStatus.OK);
        return entityResponse;
    }


    public List<Categories> findAllCategories(){
        return categoriesRepository.findAll();
    }

    public String deleteCategory(String categoryId) {

        Categories categories = categoriesRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        categoriesRepository.delete(categories);

        return "category deleted successfully";
    }


    @Transactional
    @CacheEvict(value = "products")
    public EntityResponse postProduct(ProductCreationRequest requests, List<FileUploads> uploads) {
        try {
            Inventory inventory = new Inventory();

            inventory.setStatus(InventoryStatus.INACTIVE);
            inventory.setProducts(new ArrayList<>());

            Products product = new Products();

            product.setProductName(requests.getProductName());
            product.setProductDescription(requests.getProductDescription());
            product.setInventory(inventory);




            List<Categories> categories = new ArrayList<>();
            for (String categoryName : requests.getCategoryName()){
                Categories categories1 = categoriesRepository.findCategoriesByCategoryNameIgnoreCase(categoryName);
                categories.add(categories1);
            }
            product.setCategoriesList(categories);
            categories.forEach(categories1 -> categories1.getProducts().add(product));


            List<SpecificProductDetails> detailsList = new ArrayList<>();
            int index = 0;
            for (ProductCreatedDetails details : requests.getProductCreatedDetails()){
                SpecificProductDetails details1 = new SpecificProductDetails();
                details1.setCount(details.getCount());
                details1.setColor(details.getColor());
                details1.setDiscount(details.getDiscount());
                details1.setSize(details.getSize());
                details1.setProductPrice(details.getProductPrice());

                for (FileUploads upload : uploads){
                    String[] parts = upload.getFileName().split("\\+");
                    int idx = Integer.parseInt(parts[0]);
                    if (idx == index){
                        List<ProductImages> images = setImageUrls(uploads);
                        details1.setProductImagesList(images);
                        log.info("success");
                    }
                }



                detailsList.add(details1);
                index ++;
            }
            product.setSpecificProductDetailsList(detailsList);

            inventory.getProducts().add(product);
            inventoryRepository.save(inventory);




            //productsRepository.save(product);

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

    @Transactional
    @Cacheable(value = "products", unless = "#result == null || #result.isEmpty()")
    public Page<ProductsPageResponse> searchProducts(String query, Pageable pageable) {
        Page<Products> productPage = productsRepository
                .findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(query, query, pageable);

        Page<Categories> categoryPage = categoriesRepository.findCategoriesByCategoryNameIgnoreCase(query, pageable);

        List<Products> categoryProducts = categoryPage.getContent().stream()
                .flatMap(category -> category.getProducts().stream())
                .toList();



        List<Products> combinedResults = Stream.concat(
                productPage.getContent().stream(),
                categoryProducts.stream()
        ).distinct().toList();



        List<ProductsPageResponse> responseList = combinedResults.stream()
                .map(this::mapToProductsPageResponse)
                .toList();

        // Paginate combined results
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), responseList.size());

        List<ProductsPageResponse> paginatedList = responseList.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, responseList.size());
    }

    private ProductsPageResponse mapToProductsPageResponse(Products product) {
        ProductsPageResponse response = new ProductsPageResponse();


        int rating = (int) Math.round(ratingsRepository.findAverageRatingByProductId(product.getProductId()));
        response.setProductId(product.getProductId());
        response.setProductName(product.getProductName());
        response.setRatings(rating);
        for (SpecificProductDetails details : product.getSpecificProductDetailsList()) {
            response.setProductImagesUrl(getFirstImageUrl(details.getProductImagesList()));
            float price = details.getProductPrice();
            float discount = details.getDiscount();
            response.setDiscountedPrice(price-discount);
        }

        //response.setProductImagesUrl(getFirstImageUrl(product.getSpecificProductDetailsList()
        //        .getFirst().getProductImagesList()));

        return response;
    }

    private String getFirstImageUrl(List<ProductImages> productImages) {
        return productImages != null && !productImages.isEmpty()
                ? productImages.get(0).getImageUrl()
                : null;
    }



    /*
    * Method to rate products
    * */


    @Transactional
    @CacheEvict(value = "products", allEntries = true)
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



    @Transactional
    //@Cacheable(value = "products")
    public Page<ProductsPageResponse> productsPageResponseList(Pageable pageable) {
        Page<Products> products = productsRepository.findAll(pageable);

        List<ProductsPageResponse> productsPageResponses = new ArrayList<>();

        for (Products product : products) {
            ProductsPageResponse response = new ProductsPageResponse();
            response.setProductName(product.getProductName());
            response.setProductId(product.getProductId());

            for (SpecificProductDetails details : product.getSpecificProductDetailsList()){
                double discount = details.getDiscount();
                double productPrice = details.getProductPrice();
                double price = productPrice - discount;
                response.setDiscountedPrice(price);

                if (details.getProductImagesList() != null && !details.getProductImagesList().isEmpty()) {
                    response.setProductImagesUrl(getFirstImageUrl(details.getProductImagesList()));
                }
            }


            response.setRatings(getAverageRating(product.getProductId()));
            productsPageResponses.add(response);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), productsPageResponses.size());

        List<ProductsPageResponse> paginatedList = productsPageResponses.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, productsPageResponses.size());
    }





    /*
    * method to return details of a specific product
    *
    * */


    @Transactional
    public SpecificProductResponse specificProductResponse(String productId) {

        Products products = productsRepository.findProductsByProductId(productId).orElseThrow(
                ()->new RuntimeException("the product does not exist")
        );

        SpecificProductResponse specificProductResponse = new SpecificProductResponse();
        specificProductResponse.setProductName(products.getProductName());
        specificProductResponse.setProductDescription(products.getProductDescription());
        specificProductResponse.setProductPrice(products.getSpecificProductDetailsList()
                .get(0).getProductPrice());

        List<SpecificProductDetailsResponse> detailsList = new ArrayList<>();
        List<Products> productsList = productsRepository.findAllByProductNameAndProductDescriptionLike(products.getProductName(),
                products.getProductDescription());

        for (Products products1 : productsList){
            SpecificProductDetailsResponse specs = new SpecificProductDetailsResponse();
            specs.setProductId(products1.getProductId());
            for (SpecificProductDetails details1 : products1.getSpecificProductDetailsList()){

                specs.setProductColor(details1.getColor());
                specs.setProductSize(details1.getSize());
                specs.setProductCount(details1.getCount());
                specs.setProductOldPrice(details1.getProductPrice());
                float price = details1.getProductPrice() - details1.getDiscount();
                specs.setProductPrice(price);

                List<String> imageUrls = new ArrayList<>();
                for (ProductImages productImages : details1.getProductImagesList()){
                    imageUrls.add(productImages.getImageUrl());
                }
                specs.setProductImages(imageUrls);

            }

            detailsList.add(specs);

        }
        detailsList.sort((a,b)->{
            if (a.getProductId().equals(products.getProductId())){
                return -1;
            }
            if (b.getProductId().equals(products.getProductId())){
                return 1;
            }
            return 0;
        });

        specificProductResponse.setProducts(detailsList);



        List<RelatedProducts> relatedProductsList = new ArrayList<>();

        for (Categories category : products.getCategoriesList()){
            List<Products> relatedProducts = productsRepository.findAllByProductNameContainingIgnoreCaseOrCategoriesListContainingIgnoreCase(products.getProductName(),category);
            for (Products relatedProduct : relatedProducts) {
                RelatedProducts relatedProducts1 = new RelatedProducts();
                relatedProducts1.setProductName(relatedProduct.getProductName());
                relatedProducts1.setProductPrice(relatedProduct.getSpecificProductDetailsList().get(0).getProductPrice());
                relatedProducts1.setProductImage(relatedProduct.getSpecificProductDetailsList().get(0)
                        .getProductImagesList().get(0).getImageUrl());
                relatedProductsList.add(relatedProducts1);
            }
        }


        specificProductResponse.setRelatedProducts(relatedProductsList);

        List<ProductReviews> reviewsList = new ArrayList<>();

        List<ProductRatings> ratings = ratingsRepository.findAllProductRatingsByProduct(products);

        for (ProductRatings ratings1 : ratings){
            ProductReviews productReviews = new ProductReviews();
            productReviews.setReviewContent(ratings1.getReview());
            productReviews.setRating(ratings1.getRating());
            productReviews.setUsername(ratings1.getUser().getUsername());
            reviewsList.add(productReviews);
        }
        specificProductResponse.setReviews(reviewsList);

       /* // Add pagination metadata
        specificProductResponse.setRelatedProductsPagination(new PaginationMetadata(
                relatedProductsPage.getNumber(),
                relatedProductsPage.getSize(),
                relatedProductsPage.getTotalPages(),
                relatedProductsPage.getTotalElements()
        ));*/

        return specificProductResponse;
    }



    /*
    * Method to update the products
    * */

    @Transactional
    @CacheEvict(value = "products", key = "#productId")
    public EntityResponse updateProduct(String productId, ProductCreationRequest updateRequest,List<FileUploads> uploads) {
        try {

            Products existingProduct = productsRepository.findProductsByProductId(productId).orElseThrow(
                    () -> new RuntimeException("The product with ID " + productId + " does not exist")
            );

            // Update product details
            existingProduct.setProductName(updateRequest.getProductName());
            existingProduct.setProductDescription(updateRequest.getProductDescription());

            List<Categories> updatedCategories = new ArrayList<>();
            if (updateRequest.getCategoryName() != null) {
                //settingCategory(updateRequest);
                for (String creationRequest : updateRequest.getCategoryName()){
                    Categories categories = categoriesRepository.findCategoriesByCategoryId(creationRequest);
                    Categories categories1;
                    if (categories == null){
                        categories1 = new Categories();
                        categories1.setCategoryId(creationRequest);
                        categoriesRepository.save(categories1);
                        updatedCategories.add(categories1);
                    } else {
                        categoriesRepository.delete(categories);
                        updatedCategories.add(categories);
                    }
                }

                // Clear existing categories
                existingProduct.getCategoriesList().forEach(category -> category.getProducts().remove(existingProduct));
                existingProduct.getCategoriesList().clear();

                // Add new categories
                updatedCategories.forEach(category -> category.getProducts().add(existingProduct));
                existingProduct.setCategoriesList(updatedCategories);
            }

            List<SpecificProductDetails> specificProductDetailsList = new ArrayList<>();

            int index = 0;
            for (ProductCreatedDetails details : updateRequest.getProductCreatedDetails()){
                SpecificProductDetails specificProductDetails = new SpecificProductDetails();
                specificProductDetails.setSize(details.getSize());
                specificProductDetails.setCount(details.getCount());
                specificProductDetails.setProductPrice(details.getProductPrice());
                specificProductDetails.setDiscount(details.getDiscount());
                specificProductDetails.setColor(details.getColor());
                if (uploads != null && !uploads.isEmpty()){

                    for (SpecificProductDetails details1: existingProduct.getSpecificProductDetailsList()) {
                        for (ProductImages productImages : details1.getProductImagesList()){
                            String url = productImages.getImageUrl();
                            cloudinaryService.deleteImage(url);

                            details1.getProductImagesList().forEach(image -> image.setSpecificProductDetails(null));
                            details1.getProductImagesList().clear();
                        }
                    }

                    for (FileUploads upload : uploads){
                        String[] parts = upload.getFileName().split("\\+");
                        int idx = Integer.parseInt(parts[0]);
                        if (idx == index){
                            List<ProductImages> images = setImageUrls(uploads);
                            images.forEach(image->image.setSpecificProductDetails(specificProductDetails));
                            specificProductDetails.getProductImagesList().addAll(images);
                            log.info("success");
                        }
                    }

                    /*List<ProductImages> images = setImageUrls(uploads);
                    images.forEach(image -> image.setSpecificProductDetails(specificProductDetails));
                    specificProductDetails.getProductImagesList().addAll(images);*/
                }

                specificProductDetailsList.add(specificProductDetails);
            }

            existingProduct.setSpecificProductDetailsList(specificProductDetailsList);

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

       for (SpecificProductDetails productDetails : products.getSpecificProductDetailsList()) {

           List<ProductImages> images = productDetails.getProductImagesList();

           for (ProductImages productImages : images) {
               try {
                   String imageUrl = productImages.getImageUrl();
                   cloudinaryService.deleteImage(imageUrl);
               }catch (Exception e){
                   throw new RuntimeException("could not delete image from cloudinary");
               }
           }
       }




        productsRepository.delete(products);
        return "product successfully deleted";

    }




    public int getAverageRating(String productId) {
        Double averageRating = ratingsRepository.findAverageRatingByProductId(productId);
        log.info("average rating is {}",averageRating);
        return averageRating != null ? (int) Math.round(averageRating) : 0;
    }


    @NotNull
    private List<ProductImages> setImageUrls(List<FileUploads> files) {
        List<ProductImages> productImages = new ArrayList<>();

        for (FileUploads file : files) {
            try {
                ProductImages productImage = getProductImage(file);
                if (productImage != null) {
                    productImages.add(productImage);

                }
            } catch (IOException e) {
                throw new RuntimeException("Error uploading file: ");
            }
        }
        return productImages;
    }


    private ProductImages getProductImage(FileUploads file) throws IOException {

        ProductImages productImage = new ProductImages();
        String url;

        if (file != null) {
            if (isImage(file)) {
                url = cloudinaryService.uploadImage(file);
                productImage.setImageUrl(url);
            } else if (isVideo(file)) {
                url = cloudinaryService.uploadVideo(file);
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

    private boolean isVideo(FileUploads fileStream) {
        return Objects.requireNonNull(fileStream.getFileName()).endsWith(".mp4");
    }


    private boolean isImage(FileUploads fileStream) {
        return Objects.requireNonNull(fileStream.getFileName()).toLowerCase().endsWith(".jpg")
                || Objects.requireNonNull(fileStream.getFileName()).toLowerCase().endsWith(".png")
                || Objects.requireNonNull(fileStream.getFileName()).toLowerCase().endsWith(".gif")
                || Objects.requireNonNull(fileStream.getFileName()).toLowerCase().endsWith(".jpeg");
    }






    /*private String uploadIconToCloudinary(MultipartFile iconImage) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(iconImage, ObjectUtils.emptyMap());
            return (String) uploadResult.get("url"); // Extract and return the URL
        } catch (IOException e) {
            log.error("Error uploading icon to Cloudinary: " + e.getMessage(), e);
            throw new RuntimeException("Failed to upload category icon", e);
        }
    }*/







    public void addProductQuantity(String specificProductId, int quantity) {

        SpecificProductDetails details = specificProductsRepository.findBySpecificProductId(specificProductId);
        details.setCount(details.getCount() + quantity);
        specificProductsRepository.save(details);

    }

    public void reduceProductQuantity(String specificProductId, int quantity) {

        SpecificProductDetails details = specificProductsRepository.findBySpecificProductId(specificProductId);
        details.setCount(details.getCount() - quantity);
        specificProductsRepository.save(details);

    }



    public Page<InventoryResponse> inventoryList(InventoryStatus inventoryStatus,String categoryName,
                                                 Float price1, Float price2, Pageable pageable )
    {

        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        

        if (inventoryStatus != null) {
            Page<Inventory> inventory = inventoryRepository.findAllByStatus(inventoryStatus,pageable);

            //List<InventoryResponse> inventoryResponses = new ArrayList<>();
            for (Inventory inventoryItem : inventory.getContent()) {
                for (Products products : inventoryItem.getProducts()){
                    for (SpecificProductDetails specificProductDetails : products.getSpecificProductDetailsList()) {
                        InventoryResponse inventoryResponse = new InventoryResponse();
                        inventoryResponse.setProductName(products.getProductName());
                        inventoryResponse.setStatus(inventoryItem.getStatus());
                        inventoryResponse.setImageUrl(specificProductDetails.getProductImagesList().get(0).getImageUrl());
                        inventoryResponse.setUnitPrice(specificProductDetails.getProductPrice());
                        inventoryResponses.add(inventoryResponse);
                    }
                }
            }
            
            /*int start = (int) pageable.getOffset();
            int end = (int) pageable.getOffset() + pageable.getPageSize();
            
            List<InventoryResponse> paginatedResponse = inventoryResponses.subList(start, end);
            return new PageImpl<>(paginatedResponse, pageable, inventoryResponses.size());*/
        } else if (categoryName != null) {
            Categories categories = categoriesRepository.findCategoriesByCategoryNameIgnoreCase(categoryName);
            //List<InventoryResponse> inventoryResponses = new ArrayList<>();
            for (Products products : categories.getProducts()) {
                for (SpecificProductDetails specificProductDetails : products.getSpecificProductDetailsList()) {
                    InventoryResponse inventoryResponse = getInventoryResponse(products, specificProductDetails);
                    inventoryResponses.add(inventoryResponse);
                }
            }

            /*int start = (int) pageable.getOffset();
            int end = (int) pageable.getOffset() + pageable.getPageSize();
            List<InventoryResponse> paginatedResponse = inventoryResponses.subList(start, end);
            return new PageImpl<>(paginatedResponse, pageable, inventoryResponses.size());*/
            
        } else if (price1 != null && price2 != null) {

            Page<SpecificProductDetails> productDetails = specificProductsRepository.findAllByProductPriceBetween(price1,price2,pageable);

            //List<InventoryResponse> inventoryResponses = new ArrayList<>();
            for (SpecificProductDetails specificProductDetails : productDetails.getContent()) {
                InventoryResponse inventoryResponse = getInventoryResponse(specificProductDetails);
                inventoryResponses.add(inventoryResponse);
            }
        }
        else {
            Page<Products> products = productsRepository.findAll(pageable);
            for (Products products1 : products){
                for (SpecificProductDetails details : products1.getSpecificProductDetailsList()){
                    InventoryResponse inventoryResponse = getInventoryResponse(details);
                    inventoryResponses.add(inventoryResponse);
                }
            }
        }

        int start = (int) pageable.getOffset();
        int end = (int) pageable.getOffset() + pageable.getPageSize();
        List<InventoryResponse> paginatedResponse = inventoryResponses.subList(start, end);
        return new PageImpl<>(paginatedResponse, pageable, inventoryResponses.size());


    }

    private static InventoryResponse getInventoryResponse(Products products, SpecificProductDetails specificProductDetails) {
        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setProductName(products.getProductName());
        inventoryResponse.setQuantity(specificProductDetails.getCount());
        inventoryResponse.setStatus(products.getInventory().getStatus());
        inventoryResponse.setImageUrl(specificProductDetails.getProductImagesList().get(0).getImageUrl());
        inventoryResponse.setUnitPrice(specificProductDetails.getProductPrice());
        return inventoryResponse;
    }

    private static InventoryResponse getInventoryResponse(SpecificProductDetails specificProductDetails) {
        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setProductName(specificProductDetails.getProducts().getProductName());
        inventoryResponse.setQuantity(specificProductDetails.getCount());
        inventoryResponse.setImageUrl(specificProductDetails.getProductImagesList().get(0).getImageUrl());
        inventoryResponse.setStatus(specificProductDetails.getProducts().getInventory().getStatus());
        inventoryResponse.setUnitPrice(specificProductDetails.getProductPrice());
        return inventoryResponse;
    }

    public String addToWishList(String specificProductId, String userId){

        if (usersRepository.existsByUserId(userId))
    }


}
