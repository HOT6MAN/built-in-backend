package com.example.hotsix.util;

import com.example.hotsix.service.chat.ChatMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@Component
@Scope("prototype")
public class KafkaTopicProvider {
    private final String chatroomId;
    private final SimpMessageSendingOperations messageTemplate;
    private final ChatMessageService service;

    public KafkaTopicProvider(@Value("#{chatroomId}")String chatroomId, SimpMessageSendingOperations messageTemplate
    , ChatMessageService service) {
        this.chatroomId = chatroomId;
        this.messageTemplate = messageTemplate;
        this.service = service;
    }

    public String getChatRoomId() {
        return chatroomId;
    }

    @KafkaListener(topics = "#{__listener.chatRoomId}", groupId = "chat-group")
    public void listenToRoom(@Payload String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String chatRoomId) {
        System.out.println("Received message for room " + chatRoomId + ": " + message);


        messageTemplate.convertAndSend("/sub/"+chatRoomId, message);
    }
}
