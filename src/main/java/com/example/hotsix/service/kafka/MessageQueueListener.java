package com.example.hotsix.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageQueueListener {

    @KafkaListener(topics = "message_queue2", containerFactory = "buildDeployListenerContainerFactory")
    public void listen(String message) {
        try {
            log.debug("Entering message listener");
            log.info("Message Queue Listener Message = {}", message);
            System.out.println("Received message in group 'build-deploy-group': " + message);
            log.debug("Exiting message listener");
        } catch (Exception e) {
            log.error("Error processing message: ", e);
        }
    }
}
