package com.example.hotsix.service.notification;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter subscribe(String userId, String lastEventId);
    void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data);
    boolean hasLostData(String lastEventId);
    void sendLostData(String lastEventId, String email, String emitterId, SseEmitter emitter);
    void send(String receiver, String content, String type, String urlValue);
    void sendToClient(SseEmitter emitter, String id, Object data);

}
