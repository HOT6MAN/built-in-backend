package com.example.hotsix.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@Slf4j
public class KafkaConsumerConfig {
    @Value("${kafka.bootstrap.server.config}")
    private String serverConfig;

    private Map<String, Object> getCommonConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverConfig);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return config;
    }

    @Bean
    public ConsumerFactory<String, Object> chatConsumerFactory() {
        Map<String, Object> config = getCommonConfig();
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "chat-group");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConsumerFactory<String, Object> buildDeployConsumerFactory() {
        Map<String, Object> config = getCommonConfig();
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "build-deploy-group");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConsumerFactory<String, Object> logConsumerFactory() {
        Map<String, Object> config = getCommonConfig();
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "log-group");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        log.info("Chatting 전용 Kafka Listener Container Factory 생성");
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(chatConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> buildDeployListenerContainerFactory() {
        log.info("빌드 시작 전용 Message Queue 토픽을 대상으로 한 Listener Container Factory Bean 생성");
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(buildDeployConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> logListenerContainerFactory() {
        log.info("Log 전용 토픽을 대상으로 한 Listener Container Factory Bean 생성");
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(logConsumerFactory());
        return factory;
    }
}