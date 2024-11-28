package org.codiz.onshop.service.impl.products;

import com.cloudinary.Cloudinary;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.CategoryCreationRequest;
import org.codiz.onshop.dtos.requests.ProductCreatedDetails;
import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.RatingsRequest;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.products.*;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.products.*;
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


    @Transactional
    public EntityResponse createCategory(List<CategoryCreationRequest> categoryCreationRequest) {

        List<Categories> categories = categoryCreationRequest.stream()
                .map(cat ->{
                    Categories c = new Categories();
                    c.setCategoryName(cat.getCategoryName());
                    String url = cloudinaryService.uploadImage(cat.getCategoryIcon());
                    c.setCategoryIcon(url);
                    return c;
                }).toList();
        categoriesRepository.saveAll(categories);
        EntityResponse entityResponse = new EntityResponse();
        entityResponse.setMessage("Category created successfully");
        entityResponse.setCreatedAt(Timestamp.from(Instant.now()));
        entityResponse.setStatus(HttpStatus.OK);
        return entityResponse;

    }

    @Transactional
    public EntityResponse updateCategory(String categoryId,CategoryCreationRequest categoryCreationRequest) {
        Categories categories = categoriesRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        categories.setCategoryName(categoryCreationRequest.getCategoryName());
        if (categoryCreationRequest.getCategoryIcon() != null) {
            String url = cloudinaryService.uploadImage(categoryCreationRequest.getCategoryIcon());
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
    public EntityResponse postProduct(ProductCreationRequest requests) {
        try {

            Products product = new Products();

            product.setProductName(requests.getProductName());
            product.setProductDescription(requests.getProductDescription());




            List<Categories> categories = new ArrayList<>();
            for (String categoryName : requests.getCategoryName()){
                Categories categories1 = categoriesRepository.findCategoriesByCategoryNameIgnoreCase(categoryName);
                categories.add(categories1);
            }
            product.setCategoriesList(categories);
            categories.forEach(categories1 -> categories1.getProducts().add(product));


            List<SpecificProductDetails> detailsList = new ArrayList<>();
            for (ProductCreatedDetails details : requests.getProductCreatedDetails()){
                SpecificProductDetails details1 = new SpecificProductDetails();
                details1.setCount(details.getCount());
                details1.setColor(details.getColor());
                details1.setDiscount(details.getDiscount());
                details1.setSize(details.getSize());
                details1.setProductPrice(details.getProductPrice());
                List<ProductImages> images = setImageUrls(details.getProductUrls());
                details1.setProductImagesList(images);


                detailsList.add(details1);
            }
            product.setSpecificProductDetailsList(detailsList);




            productsRepository.save(product);

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
    public EntityResponse updateProduct(String productId, ProductCreationRequest updateRequest) {
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

            for (ProductCreatedDetails details : updateRequest.getProductCreatedDetails()){
                SpecificProductDetails specificProductDetails = new SpecificProductDetails();
                specificProductDetails.setSize(details.getSize());
                specificProductDetails.setCount(details.getCount());
                specificProductDetails.setProductPrice(details.getProductPrice());
                specificProductDetails.setDiscount(details.getDiscount());
                specificProductDetails.setColor(details.getColor());
                if (details.getProductUrls() != null && !details.getProductUrls().isEmpty()){

                    for (SpecificProductDetails details1: existingProduct.getSpecificProductDetailsList()) {
                        for (ProductImages productImages : details1.getProductImagesList()){
                            String url = productImages.getImageUrl();
                            cloudinaryService.deleteImage(url);

                            details1.getProductImagesList().forEach(image -> image.setSpecificProductDetails(null));
                            details1.getProductImagesList().clear();
                        }
                    }


                    List<ProductImages> images = setImageUrls(details.getProductUrls());
                    images.forEach(image -> image.setSpecificProductDetails(specificProductDetails));
                    specificProductDetails.getProductImagesList().addAll(images);
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

    @Override
    public List<InventoryResponse> showInventory() {
        return List.of();
    }


    public int getAverageRating(String productId) {
        Double averageRating = ratingsRepository.findAverageRatingByProductId(productId);
        log.info("average rating is {}",averageRating);
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

    private boolean isVideo(MultipartFile fileStream) {
        return Objects.requireNonNull(fileStream.getOriginalFilename()).endsWith(".mp4");
    }


    private boolean isImage(MultipartFile fileStream) {
        return Objects.requireNonNull(fileStream.getOriginalFilename()).toLowerCase().endsWith(".jpg")
                || Objects.requireNonNull(fileStream.getOriginalFilename()).toLowerCase().endsWith(".png")
                || Objects.requireNonNull(fileStream.getOriginalFilename()).toLowerCase().endsWith(".gif")
                || Objects.requireNonNull(fileStream.getOriginalFilename()).toLowerCase().endsWith(".jpeg");
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





    public void addProductQuantity(String productId, int quantity) {

        Products products = productsRepository.findProductsByProductId(productId).orElseThrow(
                ()->new RuntimeException("The product with ID " + productId + " does not exist")
        );

        Inventory inventory = inventoryRepository.findByProducts(products);
        inventory.setQuantityBought((inventory.getQuantityBought() - inventory.getQuantitySold()) + quantity);
        inventory.setQuantitySold(0);
        inventory.setLastUpdate(Instant.now());
        inventoryRepository.save(inventory);

    }

    public void reduceProductQuantity(String productId, int quantity) {
        Products products = productsRepository.findProductsByProductId(productId).orElseThrow(
                ()->new RuntimeException("The product with ID " + productId + " does not exist")
        );
        Inventory inventory = inventoryRepository.findByProducts(products);
        inventory.setQuantitySold(inventory.getQuantitySold() + quantity);
    }


    /*
    * method to get all the inventory
    * */

    /*public List<InventoryResponse> showInventory() {
        List<Inventory> inventory = inventoryRepository.findAll();


        return inventory.stream()
                .filter(res -> {
                    Instant now = Instant.now();
                    return res.getLastUpdate() != null &&
                            !res.getLastUpdate().isAfter(now);
                })
                .map(res -> {
                    InventoryResponse inventoryResponse = new InventoryResponse();
                    Products products = res.getProducts();

                    inventoryResponse.setProductName(products.getProductName());
                    inventoryResponse.setSellingPrice(products.getProductPrice() - products.getDiscount());
                    inventoryResponse.setQuantitySold(res.getQuantitySold());
                    inventoryResponse.setQuantityRemaining(res.getQuantityBought() - res.getQuantitySold());
                    inventoryResponse.setLastUpdate(res.getLastUpdate());



                    double totalSold = res.getQuantitySold() * (products.getProductPrice() - products.getDiscount());


                    return inventoryResponse;
                })
                .toList();
    }

    *//*
    * method to get the inventory of a specific product
    * *//*
    public InventoryResponse showProductInventory(String productId) {
        Inventory inventory = inventoryRepository.findByProducts(productsRepository.findByProductId(productId));
        if (inventory == null) {
            log.info("the product with ID " + productId + " does not exist");
            return null;
        }
        return modelMapper.map(inventory, InventoryResponse.class);

    }*/
}
