package com.salita.productmicroservice.service;

import com.salita.productmicroservice.service.dto.CreateProductDto;

public interface ProductService {

    String createProduct(CreateProductDto createProductDto);
}
