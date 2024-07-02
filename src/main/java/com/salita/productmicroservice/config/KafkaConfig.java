package com.salita.productmicroservice.config;

import com.salita.productmicroservice.service.event.ProductCreatedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("spring.kafka.producer.bootstrap-servers")
    private String bootstrapServers;
    @Value("spring.kafka.producer.key-serializer")
    private String keySerializer;
    @Value("spring.kafka.producer.value-serializer")
    private String valueSerializer;
    @Value("spring.kafka.producer.acks")
    private String producerAcks;
    @Value("spring.kafka.producer.retries")
    private String retries;
    @Value("spring.kafka.properties.retry.backoff.ms")
    private String retryBackoffMs;
    @Value("spring.kafka.producer.properties.enable.idempotence")
    private String idempotence;
    @Value("spring.kafka.producer.properties.max.in.flight.requests.per.connection")
    private String perConnection;
    Map<String, Object> producerConfig() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        config.put(ProducerConfig.ACKS_CONFIG, producerAcks);
        config.put(ProducerConfig.RETRIES_CONFIG, retries);
        config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, retryBackoffMs);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotence);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, perConnection);

        return config;
    }

    @Bean
    ProducerFactory<String, ProductCreatedEvent> productFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }
    @Bean
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate() {
        return new KafkaTemplate<String, ProductCreatedEvent>(productFactory());
    }
    @Bean
    NewTopic createTopic() {
        return TopicBuilder.name("product-create-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

}
