package com.salita.productmicroservice.controller;

import com.salita.productmicroservice.service.ProductService;
import com.salita.productmicroservice.service.dto.CreateProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody CreateProductDto createProductDto) {
       String productId = productService.createProduct(createProductDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productId);
    }
}
