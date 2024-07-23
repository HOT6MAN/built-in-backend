package com.example.hotsix.controller.notification;

import com.example.hotsix.dto.NotificationDto;
import com.example.hotsix.service.notification.NotificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable String userId,
                                @RequestHeader(value="Last-Event-Id", required = false, defaultValue = "")String lastEventId) {
        System.out.println("call subscribe");
        return notificationService.subscribe(userId, lastEventId);
    }

    @PostMapping("/notify/{userId}")
    public void notify(@PathVariable("userId") String userId, @RequestBody NotificationDto dto){
        System.out.println("receiver = "+userId+" // noti = "+dto);
        notificationService.send(userId, dto.getContent(), dto.getNotificationType(), dto.getUrl());
    }
}
