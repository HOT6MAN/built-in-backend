package com.example.hotsix.service.notification;

import com.example.hotsix.dto.notification.GeneralResponseDto;
import com.example.hotsix.dto.notification.Notification;
import com.example.hotsix.model.NotificationDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface NotificationService {
    SseEmitter subscribe(String userId, String lastEventId);
    void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data);
    boolean hasLostData(String lastEventId);
    void sendLostData(String lastEventId, String email, String emitterId, SseEmitter emitter);
    void send(Long sender, Long receiver, String type);
    void sendToClient(SseEmitter emitter, String id, Notification data);
    void sendGeneralResponse(GeneralResponseDto data);

    Notification save(Notification notification);
    List<Notification> findAllUnreadNotificationByUserId(Long userId);
    List<Notification> findAllNotificationByUserId(Long userId);
    List<NotificationDto> findAllNotificationDtoByUserId(Long userId);
}
