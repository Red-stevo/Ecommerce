package org.codiz.onshop.service.impl.products;

import com.cloudinary.Cloudinary;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.ControllerAdvice.custom.EntityDeletionException;
import org.codiz.onshop.ControllerAdvice.custom.ResourceCreationFailedException;
import org.codiz.onshop.ControllerAdvice.custom.ResourceNotFoundException;
import org.codiz.onshop.ControllerAdvice.custom.UserDoesNotExistException;
import org.codiz.onshop.dtos.requests.*;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.products.*;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.products.*;
import org.codiz.onshop.repositories.users.UserProfilesRepository;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.codiz.onshop.service.CloudinaryService;
import org.codiz.onshop.repositories.products.PopularProductsRepository;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    private final WishListRepository wishListRepository;
    private final ProductImagesRepository productImagesRepository;
    private final PopularProductsRepository popularProductsRepository;
    private final ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder;


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
            throw new ResourceCreationFailedException("could not create category");
        }



    }

    @Transactional
    public EntityResponse updateCategory(String categoryId,String categoryName, MultipartFile fileUploads) throws IOException {
        try {
            Categories categories = categoriesRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
            categories.setCategoryName(categoryName);
            if (fileUploads != null) {
                cloudinaryService.deleteImage(categories.getCategoryIcon());
                //cloudinaryService.deleteImage()
                FileUploads uploads = new FileUploads(fileUploads.getOriginalFilename(),fileUploads.getBytes());
                String url = cloudinaryService.uploadImage(uploads);

                categories.setCategoryIcon(url);
            }
            categoriesRepository.save(categories);
            EntityResponse entityResponse = new EntityResponse();
            entityResponse.setMessage("Category updated successfully");
            entityResponse.setCreatedAt(Timestamp.from(Instant.now()));
            entityResponse.setStatus(HttpStatus.OK);
            return entityResponse;
        }catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("could not update category");
        }
    }


    public List<CategoryResponse> findAllCategories(){
        try {
            List<CategoryResponse> responses = new ArrayList<>();
        /*log.info("hahaha");
        Optional<Categories> cats = categoriesRepository.findAll().stream().findAny();
        log.info("hahaha");
        System.out.println(cats);*/

            for (Categories categories : categoriesRepository.findAll()) {
                System.out.println(categories);
                CategoryResponse categoryResponse = new CategoryResponse();
                categoryResponse.setCategoryName(categories.getCategoryName());
                categoryResponse.setCategoryId(categories.getCategoryId());
                categoryResponse.setCategoryIcon(categories.getCategoryIcon());
                responses.add(categoryResponse);
            }
            System.out.println(responses);
            return responses;
        }catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("could not find all categories");
        }
    }

    public String deleteCategory(String categoryId) throws IOException {
       try {
           log.info("service to delete category");
           Categories categories = categoriesRepository.findCategoriesByCategoryId(categoryId);
           if (categories == null) {
               throw new EntityNotFoundException();
           }
           cloudinaryService.deleteImage(categories.getCategoryIcon());
           categoriesRepository.delete(categories);

           return "category deleted successfully";
       }catch (EntityDeletionException e) {
           throw new EntityDeletionException("could not delete the category "+e.getMessage());
       }
    }


    @Transactional
    @CacheEvict(value = "products")
    public CompletableFuture<EntityResponse> postProduct(ProductCreationRequest requests, List<FileUploads> uploads) {
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
                details1.setCreatedAt(Instant.now());
                List<ProductImages> imagesList = new ArrayList<>();
                for (FileUploads upload : uploads){
                    String[] parts = upload.getFileName().split("\\+");
                    int idx = Integer.parseInt(parts[0]);
                    if (idx == index){
                        ProductImages images = setImageUrls(upload);
                        images.setSpecificProductDetails(details1);
                        imagesList.add(images);
                        productImagesRepository.save(images);
                        log.info("success");
                    }
                }
                details1.setProductImagesList(imagesList);
                details1.setProducts(product);
                /*specificProductsRepository.save(details1);*/



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

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            log.error("Error during product creation: " + e.getMessage(), e);
            throw new ResourceCreationFailedException("Error during product creation "+ e.getMessage());
        }
    }



    @Transactional
    @Cacheable(value = "products", unless = "#result == null || #result.isEmpty()")
    public Page<ProductsPageResponse> searchProducts(String query, Pageable pageable) {
        try {
            List<Products> productsList;

            if ("All Products".equalsIgnoreCase(query)) {
                // Fetch all products and shuffle
                productsList = productsRepository.findAll();
                if (productsList == null) {
                    productsList = Collections.emptyList(); // Avoid null
                }
                Collections.shuffle(productsList);
            } else {
                // Search products by name or description
                List<Products> productResults = Optional.ofNullable(productsRepository
                                .findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(query, query, pageable))
                        .map(Page::getContent)
                        .orElse(Collections.emptyList());

                // Search categories by name and retrieve associated products

                Page<Categories> categoriesList = categoriesRepository.findCategoriesByCategoryNameIgnoreCase(query, pageable);
                if (categoriesList == null) {
                    productsList = Collections.emptyList();
                }
                List<Products> categoryProducts = categoriesList
                        .stream()
                        .flatMap(category -> category.getProducts().stream())
                        .distinct()
                        .toList();

                // Search specific product details (color or size) and retrieve associated products
                Page<SpecificProductDetails> specificProductDetails = specificProductsRepository
                        .findAllByColorContainingIgnoreCaseOrSizeContainingIgnoreCase(query, query, pageable);

                if (specificProductDetails == null) {
                    productsList = Collections.emptyList();
                }
                List<Products> specificProducts = specificProductDetails
                        .stream()
                        .flatMap(sp -> Optional.ofNullable(sp.getProducts()).stream())
                        .distinct()
                        .toList();

                // Combine results and remove duplicates
                Set<Products> combinedResults = new HashSet<>();
                combinedResults.addAll(productResults);
                combinedResults.addAll(categoryProducts);
                combinedResults.addAll(specificProducts);

                productsList = new ArrayList<>(combinedResults);

                // Shuffle the combined results
                Collections.shuffle(productsList);
            }

            // Map to response DTO
            List<ProductsPageResponse> responseList = new ArrayList<>();
            if (!productsList.isEmpty()) {
                responseList = productsList.stream()
                        .map(this::mapToProductsPageResponse)
                        .filter(Objects::nonNull) // Filter out any null responses
                        .toList();
            } else {
                // Handle the case where productsList is empty
                // You can either return an empty response or throw a specific exception
                return new PageImpl<>(Collections.emptyList(), pageable, 0);
            }

// Paginate the results
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), responseList.size());
            if (start > end) {
                throw new IllegalArgumentException("Page request out of range");
            }

            List<ProductsPageResponse> paginatedList = responseList.subList(start, end);
            return new PageImpl<>(paginatedList, pageable, responseList.size());

        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid page request: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("An error occurred while fetching products.");
        }
    }




    private ProductsPageResponse mapToProductsPageResponse(Products product) {
        if (product == null) {
            return new ProductsPageResponse(); // or throw a custom exception
        }

        ProductsPageResponse response = new ProductsPageResponse();
        response.setProductId(product.getProductId());
        response.setProductName(product.getProductName());

        // Calculate ratings
        Double averageRating = ratingsRepository.findAverageRatingByProductId(product.getProductId());
        int rating = (averageRating != null) ? (int) Math.round(averageRating) : 0;
        response.setRatings(rating);


        // Check specific product details
        if (product.getSpecificProductDetailsList() != null && !product.getSpecificProductDetailsList().isEmpty()) {
            SpecificProductDetails details = product.getSpecificProductDetailsList().get(0);
            float price = details.getProductPrice();
            log.info(""+price);
            float discount = details.getDiscount();
            log.info(""+discount);

            response.setDiscountedPrice(price - discount);

            // Check product images
            if (details.getProductImagesList() != null && !details.getProductImagesList().isEmpty()) {
                response.setProductImagesUrl(details.getProductImagesList().get(0).getImageUrl());
            }
        }

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
        try{
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
        }catch (Exception e){
            throw new ResourceCreationFailedException("could not add rating");
        }
    }



/*
* Method to get products for the products page
* */



    @Transactional
    //@Cacheable(value = "products")
    public Page<ProductsPageResponse> productsPageResponseList(Pageable pageable) {
        try {
            Page<Products> products = productsRepository.findAll(pageable);

            List<ProductsPageResponse> productsPageResponses = new ArrayList<>();

            for (Products product : products) {
                ProductsPageResponse response = new ProductsPageResponse();
                response.setProductName(product.getProductName());
                response.setProductId(product.getProductId());

                response.setProductImagesUrl(product.getSpecificProductDetailsList().get(0).getProductImagesList()
                        .get(0).getImageUrl());

                float price = product.getSpecificProductDetailsList().get(0).getProductPrice();
                float discount = product.getSpecificProductDetailsList().get(0).getDiscount();

                response.setDiscountedPrice(price - discount);


                response.setRatings(getAverageRating(product.getProductId()));
                productsPageResponses.add(response);
            }

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), productsPageResponses.size());

            List<ProductsPageResponse> paginatedList = productsPageResponses.subList(start, end);

            return new PageImpl<>(paginatedList, pageable, productsPageResponses.size());
        }catch (Exception e){
            throw new ResourceNotFoundException("could not get the products");
        }
    }





    /*
    * method to return details of a specific product
    *
    * */


    @Transactional
    public SpecificProductResponse specificProductResponse(String productId) {

        try{
            Products products = null;
            if (!productsRepository.existsById(productId)) {
                log.info("the id is for specific product");
                SpecificProductDetails details = specificProductsRepository.findBySpecificProductId(productId).orElseThrow(
                        ()->new RuntimeException("the product does not exist")
                );
                productId = details.getProducts().getProductId();
            }

            log.info(""+productId);
            products = productsRepository.findProductsByProductId(productId).orElseThrow(
                    ()->new RuntimeException("the product does not exist")
            );

            SpecificProductResponse specificProductResponse = new SpecificProductResponse();
            specificProductResponse.setProductName(products.getProductName());
            specificProductResponse.setProductDescription(products.getProductDescription());
            specificProductResponse.setProductPrice(products.getSpecificProductDetailsList()
                    .get(0).getProductPrice());

            List<SpecificProductDetailsResponse> detailsList = new ArrayList<>();
            /*List<Products> productsList = productsRepository.findAllByProductNameAndProductDescriptionLike(products.getProductName(),
                    products.getProductDescription());*/

            for (SpecificProductDetails details1 : products.getSpecificProductDetailsList()){
                SpecificProductDetailsResponse specs = new SpecificProductDetailsResponse();
                specs.setProductId(details1.getSpecificProductId());

                specs.setProductColor(details1.getColor());
                specs.setProductSize(details1.getSize());
                specs.setProductCount(details1.getCount());
                specs.setProductOldPrice(details1.getProductPrice());
                specs.setProductId(details1.getSpecificProductId());
                float price = details1.getProductPrice() - details1.getDiscount();
                specs.setProductPrice(price);

                List<String> imageUrls = new ArrayList<>();
                    for (ProductImages productImages : details1.getProductImagesList()){
                        imageUrls.add(productImages.getImageUrl());
                    }
                    specs.setProductImages(imageUrls);

                detailsList.add(specs);
                log.info("list found :" +specs);

            }
            Products finalProducts = products;
            detailsList.sort((a, b) -> {
                if (a.getProductId().equals(finalProducts.getProductId())) {
                    return -1;
                }
                if (b.getProductId().equals(finalProducts.getProductId())) {
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
                    relatedProducts1.setProductId(relatedProduct.getSpecificProductDetailsList().get(0).getSpecificProductId());
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
        }catch (Exception e){
            throw new ResourceNotFoundException("could not find the product");
        }
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
            log.info("checking if the categories is null");
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
                specificProductDetails.setCreatedAt(Instant.now());
                specificProductDetails.setProducts(existingProduct);
                log.info("checking if the images are to be updated");
                if (uploads != null && !uploads.isEmpty()){
                    log.info("images present");
                    for (SpecificProductDetails details1: existingProduct.getSpecificProductDetailsList()) {
                        for (ProductImages productImages : details1.getProductImagesList()){
                            String url = productImages.getImageUrl();
                            cloudinaryService.deleteImage(url);

                            details1.getProductImagesList().forEach(image -> image.setSpecificProductDetails(null));
                            details1.getProductImagesList().clear();
                        }
                    }

                    for (FileUploads upload : uploads){
                        log.info("updating the images");
                        String[] parts = upload.getFileName().split("\\+");
                        int idx = Integer.parseInt(parts[0]);
                        if (idx == index){
                            ProductImages images = setImageUrls(upload);
                            images.setImageUrl(images.getImageUrl());
                            images.setSpecificProductDetails(specificProductDetails);
                            productImagesRepository.save(images);
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
            throw new ResourceCreationFailedException("Error during product update");
        }
    }



    /*
    * method to delete a product
    * */

    public String deleteProduct(String productId){
        try {
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
        } catch (Exception e){
            throw new EntityDeletionException("could not delete the product");
        }

    }




    public int getAverageRating(String productId) {
        Double averageRating = ratingsRepository.findAverageRatingByProductId(productId);
        log.info("average rating is {}",averageRating);
        return averageRating != null ? (int) Math.round(averageRating) : 0;
    }


    @NotNull
    private ProductImages setImageUrls(FileUploads files) {
        ProductImages productImages = new ProductImages();

        try {
            return getProductImage(files);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file: ");
        }


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

        SpecificProductDetails details = specificProductsRepository.findBySpecificProductId(specificProductId).orElseThrow(()->new ResourceNotFoundException("could not get the product"));
        details.setCount(details.getCount() + quantity);
        specificProductsRepository.save(details);

    }

    public void reduceProductQuantity(String specificProductId, int quantity) {

        SpecificProductDetails details = specificProductsRepository.findBySpecificProductId(specificProductId).orElseThrow(()->new ResourceNotFoundException("could not get product"));
        details.setCount(details.getCount() - quantity);
        specificProductsRepository.save(details);

    }



    public Page<InventoryResponse> inventoryList(InventoryStatus inventoryStatus,String categoryName,
                                                 Float price1, Float price2, Pageable pageable )
    {

        try{
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
        } catch (Exception e){
            throw new ResourceNotFoundException("could not found the inventory");
        }


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

    @Transactional
    public ResponseEntity addToWishList(String specificProductId, String userId) {
        try{
            // Check if the user exists
            if (!usersRepository.existsByUserId(userId)) {
                throw new UserDoesNotExistException("User not found");
            }

            Users user = usersRepository.findUsersByUserId(userId);
            log.info("getting specific product");
            SpecificProductDetails specificProductDetails = specificProductsRepository.findBySpecificProductId(specificProductId)
                    .orElseThrow(() -> new ResourceNotFoundException("could not get the product"));
            log.info("specific product found");

            WishList wishList = wishListRepository.findByUser(user).orElseGet(() -> {
                log.info("wishlist does not exist, creating one");
                WishList newWishList = new WishList();
                newWishList.setUser(user);
                return wishListRepository.save(newWishList);
            });
            log.info("wishlist fetched/created successfully");

            if (wishList.getSpecificProductDetails().contains(specificProductDetails)) {
                log.info("Product already in the wishlist");
                return new ResponseEntity<>(HttpStatus.OK);
            }

            wishList.getSpecificProductDetails().add(specificProductDetails);
            specificProductDetails.setWishList(wishList);

            wishListRepository.save(wishList);
            //specificProductsRepository.save(specificProductDetails);

            log.info("Product added to wishlist successfully");
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e){
            e.printStackTrace();
            throw new ResourceCreationFailedException("could not create wishlist");
        }
    }

    @Transactional
    public List<WishListResponse> getWishList(String userId){
       try{
           if (!usersRepository.existsByUserId(userId)) {
               throw new UserDoesNotExistException("User not found");
           }

           Users users = usersRepository.findUsersByUserId(userId);
           WishList wishList = wishListRepository.findByUser(users).orElseThrow(()->new ResourceNotFoundException("could not get the wishlist"));
           List<WishListResponse> responses = new ArrayList<>();
            log.info("getting specifics");

           List<SpecificProductDetails> specificProductDetailsList = wishList.getSpecificProductDetails();
            log.info(""+specificProductDetailsList);
           for (SpecificProductDetails specificProductDetails : specificProductDetailsList) {
               WishListResponse wishListResponse = new WishListResponse();
               wishListResponse.setProductName(specificProductDetails.getProducts().getProductName());
               wishListResponse.setPrice(specificProductDetails.getProductPrice() - specificProductDetails.getDiscount());
               wishListResponse.setImageUrl(specificProductDetails.getProductImagesList().get(0).getImageUrl());
               wishListResponse.setInStock(specificProductDetails.getCount() > 0);
               wishListResponse.setProductColor(specificProductDetails.getColor());
               wishListResponse.setSpecificProductId(specificProductDetails.getSpecificProductId());
               responses.add(wishListResponse);
           }
           log.info("success");

           return responses;
       }catch (Exception e){
           throw new ResourceNotFoundException("could not get wishlist");
       }

    }

    public String deleteWishListItem(String userId, String specificProductIds) {
        try {

            if (specificProductIds.isEmpty()) {
                wishListRepository.deleteWishListBySpecificProductDetails(specificProductsRepository.findBySpecificProductId(specificProductIds).get());
            }

            return "Products removed from wishlist successfully";
        } catch (Exception e){
            throw new EntityDeletionException("could not delete wishlist");
        }
    }

    public List<DiscountedProductsResponse> findDiscountedProducts( int size) {
        try {
            List<SpecificProductDetails> productDetails = specificProductsRepository.findAll();

            // Filter and sort products by discount in descending order
            // Sort by discount descending

            return productDetails.stream()
                    .filter(product -> product.getDiscount() > 0)
                    .sorted(Comparator.comparing(SpecificProductDetails::getDiscount).reversed())
                    .limit(size)
                    .map(product -> {
                        DiscountedProductsResponse response = new DiscountedProductsResponse();
                        response.setProductName(product.getProducts().getProductName());
                        response.setProductId(product.getSpecificProductId());
                        response.setRatings(getAverageRating(product.getProducts().getProductId()));
                        response.setProductImagesUrl(product.getProductImagesList().get(0).getImageUrl());
                        response.setDiscountedPrice(product.getProductPrice() - product.getDiscount());
                        response.setDiscount(product.getDiscount());
                        return response;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new ResourceNotFoundException("Could not find discounted products");
        }
    }


    public List<ProductsPageResponse> popularProducts(){

        return null;
    }


    public List<ProductsPageResponse> newProducts(){

        try{
            List<SpecificProductDetails> productDetails = specificProductsRepository.findAll();
            return productDetails.stream()
                    .sorted(Comparator.comparing(SpecificProductDetails::getCreatedAt).reversed())
                    .limit(10)
                    .map(product -> {
                        ProductsPageResponse response = new ProductsPageResponse();
                        response.setProductName(product.getProducts().getProductName());
                        response.setProductId(product.getSpecificProductId());
                        response.setRatings(getAverageRating(product.getProducts().getProductId()));
                        response.setProductImagesUrl(product.getProductImagesList().get(0).getImageUrl());
                        response.setDiscountedPrice(product.getProductPrice() - product.getDiscount());
                        return response;
                    })
                    .toList();
        }catch (Exception e){
            throw new ResourceNotFoundException("could not get new products");
        }

    }



}
