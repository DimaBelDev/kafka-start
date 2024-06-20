package com.salita.productmicroservice.service.impl;

import com.salita.productmicroservice.service.ProductService;
import com.salita.productmicroservice.service.dto.CreateProductDto;
import com.salita.productmicroservice.service.event.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {

    private KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductDto createProductDto) {
        //TODO save to database
        String productId = UUID.randomUUID().toString();



        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, createProductDto.getTitle(),
                createProductDto.getPrice(), createProductDto.getQuantity());

        CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate.send("product-create-events-topic", productId,  productCreatedEvent);

        future.whenComplete((result, exception) -> {
                    if (exception != null) {
                     LOGGER.error("Failed to sent message:{}", exception.getMessage());
                    } else {
                     LOGGER.info("Message sent successfully: {}", result.getRecordMetadata());
                    }
                });

        LOGGER.info("Return: {}", productId);

        return productId;
    }
}
