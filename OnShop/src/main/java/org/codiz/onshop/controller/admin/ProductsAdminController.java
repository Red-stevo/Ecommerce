package org.codiz.onshop.controller.admin;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.FileUploads;
import org.codiz.onshop.dtos.requests.InventoryRequestFilter;
import org.codiz.onshop.dtos.requests.ProductCreationRequest;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.products.InventoryStatus;
import org.codiz.onshop.service.serv.products.ProductsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/products")
public class ProductsAdminController {
    private final ProductsService productsService;

    public ProductsAdminController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping(value = "/post",consumes = "multipart/form-data")
    public ResponseEntity<CompletableFuture<EntityResponse>> postProduct(
            @RequestPart("productData") String productData, @RequestPart List<MultipartFile> files) throws JsonProcessingException {

        //deserializing the product images data
        ObjectMapper objectMapper = new ObjectMapper();
        ProductCreationRequest productCreationRequest = objectMapper.readValue(productData, ProductCreationRequest.class);

        List<FileUploads> uploads = files.stream().map(
                file -> {
                    try{
                        return new FileUploads(file.getOriginalFilename(),file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).toList();


        log.info(productData);
        files.forEach((file) -> log.info(file.getOriginalFilename()));

        log.info("done");

        return ResponseEntity.ok(productsService.postProduct(productCreationRequest,uploads));

    }

    @PutMapping(value = "/update",consumes = "multipart/form-data")
    public ResponseEntity<EntityResponse> updateProduct(
            @RequestPart("productData") String productData,
            @RequestPart List<MultipartFile> files,
            @RequestParam String productId) throws JsonProcessingException {

        //deserializing the product images data
        ObjectMapper objectMapper = new ObjectMapper();
        ProductCreationRequest productCreationRequest = objectMapper.readValue(productData, ProductCreationRequest.class);

        List<FileUploads> uploads = files.stream().map(
                file -> {
                    try{
                        return new FileUploads(file.getOriginalFilename(),file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).toList();


        log.info(productData);
        files.forEach((file) -> log.info(file.getOriginalFilename()));

        log.info("done");

        return ResponseEntity.ok(productsService.updateProduct(productId,productCreationRequest,uploads));

    }


    @PostMapping(value = "/create-category" /*consumes = "multipart/form-data"*/)
    public ResponseEntity<EntityResponse> createCategory(
            @RequestPart MultipartFile files,
            @RequestParam String filenames
    ){

        return ResponseEntity.ok(productsService.createCategory(filenames,files));
    }

    @PutMapping("/update-categories")
    public ResponseEntity<EntityResponse> updateCategory(
            @RequestParam String categoryId,
            @RequestParam String categoryName,
            @RequestPart(required = false)  MultipartFile fileUploads) throws IOException {

        return ResponseEntity.ok(productsService.updateCategory(categoryId,categoryName,fileUploads));
    }


    @DeleteMapping("delete-product")
    public ResponseEntity<HttpStatus> deleteProduct(@RequestParam String productId){
        return ResponseEntity.ok(productsService.deleteProduct(productId));
    }

    @DeleteMapping("/delete-category")
    public ResponseEntity<String> deleteCategory(@RequestParam String categoryId) throws IOException {
        return ResponseEntity.ok(productsService.deleteCategory(categoryId));
    }



    @GetMapping("/show-inventory")
    public ResponseEntity<PagedModel<EntityModel<InventoryResponse>>> inventoryList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            PagedResourcesAssembler<InventoryResponse> assembler){

        Pageable pageable = PageRequest.of(page, size);
        Page<InventoryResponse> resp = productsService.inventoryList(pageable);
        PagedModel<EntityModel<InventoryResponse>> model = assembler.toModel(resp);

        return ResponseEntity.ok(model);

    }


    @DeleteMapping("/delete-product-image")
    public ResponseEntity<HttpStatus> deleteProductImage(@RequestParam String image){
        return ResponseEntity.ok(productsService.deleteProductImage(image));
    }



}
