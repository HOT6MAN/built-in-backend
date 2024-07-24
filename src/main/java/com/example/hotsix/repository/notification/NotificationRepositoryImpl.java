package com.example.hotsix.repository.notification;

import com.example.hotsix.dto.notification.Notification;
import com.example.hotsix.service.notification.NotificationService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.example.hotsix.dto.notification.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    private final JPAQueryFactory queryFactory;

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    @Override
    public Map<String, SseEmitter> findAllEmittersByUserId(Long userId){
        return emitters.entrySet().stream()
                .filter(entry-> entry.getKey().startsWith(String.valueOf(userId)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByEmail(String email) {
        return emitters.entrySet().stream() //여러개의 Emitter가 존재할 수 있기떄문에 stream 사용
                .filter(entry -> entry.getKey().startsWith(email))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    @Override
    public Map<String, Object> findAllEventCacheStartWithByEmail(String email) {
        return emitters.entrySet().stream() //여러개의 Emitter가 존재할 수 있기떄문에 stream 사용
                .filter(entry -> entry.getKey().startsWith(email))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    @Override
    public void deleteAllEmitterStartWithId(String email) {
        emitters.forEach((key, emitter) -> {
            if (key.startsWith(email)){
                emitters.remove(key);
            }
        });
    }

    @Override
    public void deleteAllEventCacheStartWithId(String email) {
        emitters.forEach((key, emitter) -> {
            if (key.startsWith(email)){
                emitters.remove(key);
            }
        });
    }

    @Override
    public List<Notification> findAllUnreadNotificationByUserId(Long userId) {
        return queryFactory.selectFrom(notification)
                .where(
                        notification.receiver.eq(userId).and(
                                notification.isRead.eq(false)
                        )
                )
                .fetch();
    }

    @Override
    public List<Notification> findAllNotificationByUserId(Long userId) {
        return queryFactory.selectFrom(notification)
                .where(notification.receiver.eq(userId))
                .fetch();
    }
}
