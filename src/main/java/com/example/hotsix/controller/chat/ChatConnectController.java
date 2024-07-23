package com.example.hotsix.controller.chat;

import com.example.hotsix.service.chat.ChatMessageService;
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
    

    @MessageMapping("/{chatroomId}")
    public void sendMessage(@DestinationVariable("chatroomId") String chatroomId, @Payload ChatMessageVo chatMessage){
        System.out.println("chatroomId = "+chatroomId);
        System.out.println("message = "+chatMessage);
        String userId = chatMessage.getSender();
        System.out.println("called by STOMP " + chatroomId);

        KafkaTopicProvider provider = applicationContext.getBean(KafkaTopicProvider.class, chatroomId, operations, service);

        if(chatMessage.getType().equals(ChatMessageVo.MessageType.ENTER)){
            chatMessage.setContent(userId + "님이 입장하셨습니다.");
        }
        else if(chatMessage.getType().equals(ChatMessageVo.MessageType.QUIT)){
            chatMessage.setContent(userId + "님이 퇴장하셨습니다.");
        } else {
            System.out.println("call Chat Type.");
            chatMessage.setSendDate(LocalTimeUtil.getDateTime());
            chatMessage.setDescSendDate(LocalTimeUtil.getDescDateTime());
        }

        kafkaTemplate.send(chatroomId, new Gson().toJson(chatMessage));
    }
}
