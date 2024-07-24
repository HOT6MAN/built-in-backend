package com.example.hotsix.controller.chat;

import com.example.hotsix.service.chat.ChatMessageService;
import com.example.hotsix.service.chat.ChatRoomService;
import com.example.hotsix.util.KafkaTopicProvider;
import com.example.hotsix.util.LocalTimeUtil;
import com.example.hotsix.vo.ChatMessageVo;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ChatConnectController {
    private final ChatMessageService service;
    private final SimpMessageSendingOperations operations;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ApplicationContext applicationContext;
    private final ChatRoomService chatRoomService;
    

    @MessageMapping("/{chatroomId}")
    public void sendMessage(@DestinationVariable("chatroomId") String chatroomId, @Payload ChatMessageVo chatMessage){
        String userId = chatMessage.getSender();
        System.out.println("receive Message = "+chatMessage);
        chatMessage.setSendDate(LocalTimeUtil.getDateTime());
        chatMessage.setDescSendDate(LocalTimeUtil.getDescDateTime());

        if(!chatMessage.getReceiverStatus()){
            chatRoomService.updateUnreadCount(Long.parseLong(chatMessage.getChatroomId()),
                    Long.parseLong(chatMessage.getReceiver()), 1);
        }


        KafkaTopicProvider provider = applicationContext.getBean(KafkaTopicProvider.class, chatroomId, operations, service);

        kafkaTemplate.send(chatroomId, new Gson().toJson(chatMessage));
    }
}
