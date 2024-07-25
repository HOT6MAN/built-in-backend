package com.example.hotsix.service.notification;

import com.example.hotsix.dto.notification.Notification;
import com.example.hotsix.repository.notification.NotificationRepository;
import com.example.hotsix.repository.notification.NotificationRepositoryCustom;
import com.example.hotsix.util.LocalTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository repository;


    @Override
    public Notification save(Notification notification){
        repository.save(notification);
        return notification;
    }

    @Override
    public List<Notification> findAllUnreadNotificationByUserId(Long userId){
        return repository.findAllUnreadNotificationByUserId(userId);
    }

    @Override
    public List<Notification> findAllNotificationByUserId(Long userId) {
        return repository.findAllNotificationByUserId(userId);
    }


    @Override
    public SseEmitter subscribe(String userId, String lastEventId) {
        String emitterId = userId+"_"+System.currentTimeMillis();
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        if(repository.findAllNotificationByUserId(Long.parseLong(userId)) != null){
            repository.deleteAllEmitterStartWithId(userId);
        }

        emitter = repository.save(emitterId, emitter);
        emitter.onCompletion(() -> repository.deleteById(emitterId));
        emitter.onTimeout(() -> repository.deleteById(emitterId));

        String eventId = userId + "_" + System.currentTimeMillis();
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userId + "]");

        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }

        return emitter;
    }

    @Override
    public void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("open")
                    .data(data, MediaType.APPLICATION_JSON));
        } catch (IOException exception) {
            repository.deleteById(emitterId);
            emitter.completeWithError(exception);
        }
    }

    @Override
    public boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    @Override
    public void sendLostData(String lastEventId, String email, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = repository.findAllEventCacheStartWithByEmail(email);
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    @Override
    public void send(Long sender, Long receiver, String type) {
        Notification notification = Notification.builder()
                .receiver(receiver)
                .isRead(false)
                .notifyDate(LocalTimeUtil.getDateTime())
                .build();
        if ("chat".equals(type)) {
            notification.setType("chat");
            notification.setUrl("chat");
        }
        else if("invite".equals(type)){
            notification.setType("invite");
            notification.setUrl("invite");
        }
        else if("join".equals(type)){
            notification.setType("join");
            notification.setType("join");
        }
        Map<String, SseEmitter> sseEmitters = repository.findAllEmittersByUserId(receiver);
        // 로그인 한 유저의 SseEmitter 모두 가져오기

        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    repository.saveEventCache(key, notification);
                    // 데이터 전송
                    sendToClient(emitter, key, notification);
                }
        );
    }

    @Override
    public void sendToClient(SseEmitter emitter, String id, Notification data) {
        System.out.println("Before Send Event Check Type : "+data.getType());
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name(data.getType())
                    .data(data, MediaType.APPLICATION_JSON)
                    .reconnectTime(0));
        } catch (Exception exception) {
            repository.deleteById(id);
            emitter.completeWithError(exception);
        }
    }
}
