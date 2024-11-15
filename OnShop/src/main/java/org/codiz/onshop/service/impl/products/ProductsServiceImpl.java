package org.codiz.onshop.service.impl.products;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.requests.ProductDocument;
import org.codiz.onshop.entities.products.Categories;
import org.codiz.onshop.entities.products.ProductImages;
import org.codiz.onshop.entities.products.Products;
import org.codiz.onshop.repositories.products.CategoriesRepository;
import org.codiz.onshop.repositories.products.ProductImagesRepository;
import org.codiz.onshop.repositories.products.ProductSearchRepository;
import org.codiz.onshop.repositories.products.ProductsJpaRepository;
import org.codiz.onshop.service.CloudinaryService;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {
    private final ProductsJpaRepository productsRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;
    private final ProductImagesRepository productImagesRepository;
    private final ProductSearchRepository searchRepository;
    private final CategoriesRepository categoriesRepository;

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

                List<Categories> categories = request.getCategoryCreationRequestList().stream().map(request1 -> {
                    Optional<Categories> existingCategory = categoriesRepository
                            .findCategoriesByCategoryNameIgnoreCase(request1.getCategoryName());

                    return existingCategory.orElseGet(() -> {
                        Categories newCategory = new Categories();
                        newCategory.setCategoryName(request1.getCategoryName());

                        // Upload category icon to Cloudinary
                        if (request1.getCategoryIcon() != null) {
                            String iconUrl = uploadIconToCloudinary(request1.getCategoryIcon());
                            newCategory.setCategoryIcon(iconUrl); // Store the icon URL
                        }

                        return categoriesRepository.save(newCategory); // Save and return the new category
                    });
                }).toList();
                product.setCategories(categories);
                categories.forEach(categories1 -> categories1.getProducts().add(product));

                return product;
            }).toList();

            // Save products with images
            products.forEach(product -> {
                productsRepository.save(product); // Cascade saves product images if configured correctly
                product.getProductImages().forEach(image -> image.getProducts().add(product)); // Maintain bidirectional relationship
            });

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



        } catch (Exception e) {
            log.error("Error during product creation: " + e.getMessage(), e);
            throw new RuntimeException("Error during product creation", e);
        }
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
                // Handle the exception appropriately
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
                return null; // Return null for unsupported types
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

    public List<ProductDocument> searchProducts(String query) {

        return searchRepository.search(query);

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
