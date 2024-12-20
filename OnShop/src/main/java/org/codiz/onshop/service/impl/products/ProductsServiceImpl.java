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

            inventory.setStatus(InventoryStatus.ACTIVE);
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
                details1.setVariety(details.getColor());
                details1.setDiscount(details.getDiscount());
                details1.setProportion(details.getSize());
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
                productsList.stream()
                                .filter(products -> products.getInventory().getStatus()==InventoryStatus.ACTIVE)
                        .toList();

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
                        .findAllByVarietyContainingIgnoreCaseOrProportionContainingIgnoreCase(query, query, pageable);

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
                productsList.stream()
                        .filter(products -> products.getInventory().getStatus()==InventoryStatus.ACTIVE)
                        .toList();

                // Shuffle the combined results
                Collections.shuffle(productsList);
            }

            // Map to response DTO
            List<ProductsPageResponse> responseList;
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
                specs.setId(details1.getSpecificProductId());

                specs.setProductColor(details1.getVariety());
                specs.setProductSize(details1.getProportion());
                specs.setProductCount(details1.getCount());
                specs.setProductOldPrice(details1.getProductPrice());
                specs.setId(details1.getSpecificProductId());
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
                if (a.getId().equals(finalProducts.getProductId())) {
                    return -1;
                }
                if (b.getId().equals(finalProducts.getProductId())) {
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
    * method to delete a product
    * */
    @Transactional
    public HttpStatus deleteProduct(String productId){
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
                        log.info("deleted from cloudinary");
                    }catch (Exception e){
                        throw new RuntimeException("could not delete image from cloudinary");
                    }
                }
            }




            productsRepository.delete(products);
            return HttpStatus.OK;
        } catch (Exception e){
            e.printStackTrace();
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



    @Transactional
    public Page<InventoryResponse> inventoryList(Pageable pageable) {
        try {
            // Fetch paginated products directly
            Page<Products> productsPage = productsRepository.findAll(pageable);

            // Transform Products to InventoryResponse
            /*List<InventoryResponse> inventoryResponses = productsPage.stream()
                    .flatMap(product -> product.getSpecificProductDetailsList().stream()
                            .map(this::getInventoryResponse))
                    .collect(Collectors.toList());*/

            List<InventoryResponse> inventoryResponses = productsPage.stream()
                            .map(products -> {
                                SpecificProductDetails details = products.getSpecificProductDetailsList().get(0);

                                return  getInventoryResponse(details);

                            }).toList();
            log.info("the inventory :" + inventoryResponses);
            log.info("got the inventory");
            // Return a new Page object with transformed content
            return new PageImpl<>(inventoryResponses, pageable, inventoryResponses.size());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("Could not find the inventory");
        }
    }



    private InventoryResponse getInventoryResponse(SpecificProductDetails specificProductDetails) {
        InventoryResponse inventoryResponse = new InventoryResponse();

        // Safely get product images, avoiding IndexOutOfBoundsException
        String imageUrl = specificProductDetails.getProductImagesList() != null
                && !specificProductDetails.getProductImagesList().isEmpty()
                ? specificProductDetails.getProductImagesList().get(0).getImageUrl()
                : null;

        inventoryResponse.setProductName(specificProductDetails.getProducts().getProductName());
        inventoryResponse.setQuantity(specificProductDetails.getCount());
        inventoryResponse.setImageUrl(imageUrl);
        inventoryResponse.setStatus(specificProductDetails.getProducts().getInventory().getStatus().ordinal() + 1);
        inventoryResponse.setUnitPrice(specificProductDetails.getProductPrice());
        inventoryResponse.setProductId(specificProductDetails.getSpecificProductId());

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

            if (wishList.getSpecificProductDetails() != null &&wishList.getSpecificProductDetails().contains(specificProductDetails)) {
                log.info("Product already in the wishlist");
                return new ResponseEntity<>(HttpStatus.OK);
            }

            if (wishList.getSpecificProductDetails() == null) {
                wishList.setSpecificProductDetails( new ArrayList<>());
            }

            wishList.getSpecificProductDetails().add(specificProductDetails);
            specificProductDetails.setWishList(wishList);
            specificProductsRepository.save(specificProductDetails);
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
               wishListResponse.setProductColor(specificProductDetails.getVariety());
               wishListResponse.setSpecificProductId(specificProductDetails.getSpecificProductId());
               responses.add(wishListResponse);
           }
           log.info("success");

           return responses;
       }catch (Exception e){
           e.printStackTrace();
           throw new ResourceNotFoundException("could not get wishlist");
       }

    }

    public HttpStatus deleteWishListItem(String userId, String specificProductId) {
        try {
            log.info("Deleting wishlist item for userId: {}, specificProductId: {}", userId, specificProductId);

            if (specificProductId != null && !specificProductId.isEmpty()) {
                log.info("getting the specific product");
                SpecificProductDetails specificProductDetails = specificProductsRepository.findBySpecificProductId(specificProductId)
                        .orElseThrow(() -> new ResourceNotFoundException("could not get the specific product"));
                log.info("got the specific product");

                WishList wishList = specificProductDetails.getWishList();

                if (wishList != null) {

                    wishList.getSpecificProductDetails().remove(specificProductDetails);

                    specificProductDetails.setWishList(null);

                    // Save the updated wishlist
                    wishListRepository.save(wishList);

                    // Optionally delete the SpecificProductDetails entity if it's not used elsewhere
                    specificProductsRepository.save(specificProductDetails);

                    log.info("Specific product removed from wishlist successfully");
                    return HttpStatus.OK;
                } else {
                    log.warn("No wishlist found for specific product");
                    return HttpStatus.NOT_FOUND;
                }
            } else {

                WishList wishList = wishListRepository.findByUser(usersRepository.findUsersByUserId(userId))
                        .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));
                wishList.getSpecificProductDetails().removeAll(wishList.getSpecificProductDetails());
                wishListRepository.delete(wishList);

                log.info("Wishlist deleted successfully");
                return HttpStatus.OK;
            }
        } catch (Exception e) {
            log.error("Error deleting wishlist item: ", e);
            throw new EntityDeletionException("Could not delete wishlist");
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


    public List<ProductsPageResponse> popularProducts(int size){

        try {

            List<ProductsPageResponse> pageResponses = new ArrayList<>();
            List<PopularProducts> products = popularProductsRepository.findAll();

            products = products.stream().sorted(Comparator.comparing(PopularProducts::getCount).reversed()).limit(size).toList();

            products.forEach(product -> {
                ProductsPageResponse response = new ProductsPageResponse();
                response.setProductName(product.getSpecificProductDetails().getProducts().getProductName());
                response.setProductId(product.getSpecificProductDetails().getProducts().getProductId());
                response.setRatings(getAverageRating(product.getSpecificProductDetails().getProducts().getProductId()));
                response.setProductImagesUrl(product.getSpecificProductDetails().getProductImagesList().get(0).getImageUrl());
                response.setDiscountedPrice(product.getSpecificProductDetails().getProductPrice()-product.getSpecificProductDetails().getDiscount());
                pageResponses.add(response);
            });
            return pageResponses;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }


    public List<ProductsPageResponse> newProducts(int size){

        try{
            List<SpecificProductDetails> productDetails = specificProductsRepository.findAll();
            return productDetails.stream()
                    .sorted(Comparator.comparing(SpecificProductDetails::getCreatedAt).reversed())
                    .limit(size)
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

    @Transactional
    @CacheEvict(value = "products")
    public HttpStatus deleteProductImage(String image) {

        try{
            log.info("deleting image for url :"+image);
            ProductImages img = productImagesRepository.findByImageUrl(image).orElse(null);
            System.out.println(img);
            if (img != null) {
                cloudinaryService.deleteImage(image);
                productImagesRepository.deleteByImageUrl(image);
            }
            log.info("deleted sucessfully");
           return HttpStatus.OK;
        }catch (Exception e){
            throw new EntityDeletionException("Could not update product image");
        }

    }

    @Transactional
    public SpecificInventoryProductResponse getInventoryProduct(String specificProductId) {
        try {
            log.info("Fetching product details for ID: {}", specificProductId);

            SpecificProductDetails details = specificProductsRepository.findBySpecificProductId(specificProductId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Product with ID %s not found", specificProductId)));

            SpecificInventoryProductResponse response = new SpecificInventoryProductResponse();
            response.setProductName(details.getProducts().getProductName());
            response.setProductId(details.getProducts().getProductId());
            response.setProductDescription(details.getProducts().getProductDescription());

            response.setProductCategory(details.getProducts().getCategoriesList()
                    .stream()
                    .map(categories -> categories.getCategoryName())
                    .toList());

            List<SpecificProductDetailsResponse> spRes = details.getProducts().getSpecificProductDetailsList()
                    .stream()
                    .map(this::mapToSpecificProductDetailsResponse)
                    .toList();


            response.setSpecificProducts(spRes);
            log.info("success");

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error fetching product details for ID: {}", specificProductId, e);
            throw new ResourceNotFoundException(
                    String.format("Could not retrieve inventory product with ID %s", specificProductId));
        }
    }

    private SpecificProductDetailsResponse mapToSpecificProductDetailsResponse(SpecificProductDetails spec) {
        SpecificProductDetailsResponse response = new SpecificProductDetailsResponse();
        response.setId(spec.getSpecificProductId());
        response.setProductColor(spec.getVariety());
        response.setProductSize(spec.getProportion());
        response.setProductCount(spec.getCount());
        response.setProductPrice(spec.getProductPrice());
        response.setDiscount(spec.getDiscount());
        response.setProductCount(spec.getCount());
        response.setStatus(spec.getProducts().getInventory().getStatus().ordinal()+1);
        response.setProductImages(spec.getProductImagesList()
                .stream()
                .map(img -> img.getImageUrl())
                .toList());
        return response;
    }

    @Transactional
    public HttpStatus updateProduct(ProductUpdateRequest request,List<MultipartFile> uploads) {
        try {
            // Validate product existence
            log.info(":::::"+request + "::::::" +uploads);
            /*if (!productsRepository.existsById(request.getProductId())) {
                return HttpStatus.NOT_FOUND;
            }*/

            // Fetch the product
            log.info("fetching the product");
            Products products = productsRepository.findProductsByProductId(request.getProductId()).orElse(null);
            log.info("success");
            if (products == null) {
                log.error("product not found");
                return HttpStatus.NOT_FOUND;
            }

            // Update product details
            log.info("updating the product");
            updateProductDetails(request, products);

            // Update specific product details if provided
            log.info("checking if specific product details exists");
            if (request.getProductUpdates() != null) {
                log.info("updating the product");
                updateSpecificProductDetails(request.getProductUpdates(),uploads);
            }
            log.info("updated successfully");
            return HttpStatus.OK;

        } catch (Exception e) {
            log.error("Error updating product with ID: {}", request.getProductId(), e);
            throw new RuntimeException("Failed to update product", e); // Consider a custom exception
        }
    }

    private void updateProductDetails(ProductUpdateRequest request, Products products) {
        if (request.getProductName() != null) {
            products.setProductName(request.getProductName());
        }

        if (request.getProductDescription() != null) {
            products.setProductDescription(request.getProductDescription());
        }

        if (request.getCategoryName() != null) {
            products.getCategoriesList().clear();
            for (String categoryName : request.getCategoryName()) {
                Categories categories = categoriesRepository.findCategoriesByCategoryNameIgnoreCase(categoryName);
                products.getCategoriesList().add(categories);
            }
        }

        if (request.getInventoryStatus() != null) {
            if (request.getInventoryStatus() == 0)
                products.getInventory().setStatus(InventoryStatus.ACTIVE);
            else
                products.getInventory().setStatus(InventoryStatus.INACTIVE);
        }

        productsRepository.save(products);
    }

    private void updateSpecificProductDetails(SpecificProductUpdate productUpdates,List<MultipartFile> uploads) {
        SpecificProductDetails specificProductDetails = specificProductsRepository
                .findBySpecificProductId(productUpdates.getSpecificProductId())
                .orElse(null);

        if (specificProductDetails != null) {
            specificProductDetails.setProductPrice(productUpdates.getProductPrice());
            specificProductDetails.setCount(productUpdates.getCount());
            specificProductDetails.setDiscount(productUpdates.getDiscount());
            specificProductDetails.setVariety(productUpdates.getColor());
            specificProductDetails.setProportion(productUpdates.getSize());

            if (uploads != null) {
                updateProductImages(specificProductDetails, uploads);
            }

            specificProductsRepository.save(specificProductDetails);
        }
    }

    private void updateProductImages(SpecificProductDetails specificProductDetails, List<MultipartFile> productUrls) {
        List<ProductImages> imagesList = specificProductDetails.getProductImagesList();

        // Delete existing images from Cloudinary and database
        imagesList.forEach(image -> {
            try {
                cloudinaryService.deleteImage(image.getImageUrl());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            productImagesRepository.delete(image);
        });

        // Add new images
        productUrls.forEach(upload -> {
            ProductImages productImage = new ProductImages();
            String imageUrl = null;
            try {
                imageUrl = setImageUrls(new FileUploads(upload.getOriginalFilename(), upload.getBytes())).getImageUrl();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            productImage.setImageUrl(imageUrl);
            productImage.setSpecificProductDetails(specificProductDetails);
            productImagesRepository.save(productImage);
        });
    }








}
