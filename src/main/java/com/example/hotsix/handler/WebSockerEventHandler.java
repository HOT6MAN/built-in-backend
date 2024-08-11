package com.example.hotsix.handler;

import com.example.hotsix.dto.chat.ChatRoomStatus;
import com.example.hotsix.service.chat.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@RequiredArgsConstructor
public class WebSockerEventHandler {
    private final ChatRoomService chatRoomService;
    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        System.out.println("WebSocket Subscribe Listener Destination = "+destination);
        if(destination != null && destination.startsWith("/ws/log")){
            return;
        }
        if (destination != null && destination.startsWith("/sub/status")) {
            Long userId = Long.parseLong(headerAccessor.getFirstNativeHeader("userId"));
            Long chatRoomId = Long.parseLong(headerAccessor.getFirstNativeHeader("chatroomId"));
            Long receiverId = Long.parseLong(headerAccessor.getFirstNativeHeader("receiverId"));

            boolean userStatus = chatRoomService.findUser(chatRoomId, receiverId).getOnline();

            messageTemplate.convertAndSend("/sub/status/" + chatRoomId, userStatus);
        }
    }
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        System.out.println("WebSocket Connect Listener destination: " + destination);
        if (destination != null && destination.startsWith("/ws/log")) {
            return;
        }
        Long userId = Long.parseLong(headerAccessor.getFirstNativeHeader("userId"));
        Long chatRoomId = Long.parseLong(headerAccessor.getFirstNativeHeader("chatroomId"));
        Long receiverId = Long.parseLong(headerAccessor.getFirstNativeHeader("receiverId"));
        System.out.println("Connected userId: " + userId + " // chatroomId = "+chatRoomId + "// receiverId = "+receiverId);

        headerAccessor.getSessionAttributes().put("userId", userId);
        headerAccessor.getSessionAttributes().put("chatRoomId", chatRoomId);
        headerAccessor.getSessionAttributes().put("receiverId", receiverId);

        chatRoomService.updateOnlineStatus(chatRoomId, userId);

        chatRoomService.updateUnreadCount(chatRoomId, userId);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        if (destination != null && destination.startsWith("/ws/log")) {
            return;
        }
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        Long chatRoomId = (Long) headerAccessor.getSessionAttributes().get("chatRoomId");
        chatRoomService.updateOfflineStatus(chatRoomId, userId);
        System.out.println("change user online status to offline");

        messageTemplate.convertAndSend("/sub/status/" + chatRoomId, false);
    }

}
