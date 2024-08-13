package com.example.hotsix.repository.notification;

import com.example.hotsix.dto.notification.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

public interface NotificationRepositoryCustom {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void saveEventCache(String eventCacheId, Object event);
    Map<String, SseEmitter> findAllEmitterStartWithByEmail(String email);
    Map<String, Object> findAllEventCacheStartWithByEmail(String email);
    void deleteById(String id);
    void deleteAllEmitterStartWithId(String email);
    void deleteAllEventCacheStartWithId(String email);
    Map<String, SseEmitter> findAllEmittersByUserId(Long userId);

    List<Notification> findAllUnreadNotificationByUserId(Long userId);
    List<Notification> findAllNotificationByUserId(Long userId);

    Notification findNotificationByNotificationId(Long notificationId);
    void deleteNotificationByNotificationId(Long notificationId);
}
