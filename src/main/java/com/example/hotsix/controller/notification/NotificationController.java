package com.example.hotsix.controller.notification;

import com.example.hotsix.dto.notification.Notification;
import com.example.hotsix.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/notify")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable String userId,
                                @RequestHeader(value="Last-Event-Id", required = false, defaultValue = "")String lastEventId) {
        System.out.println("call subscribe");
        return notificationService.subscribe(userId, lastEventId);
    }

    @PostMapping("/{userId}")
    public void notify(@PathVariable("userId") String userId, @RequestBody Notification dto){
        System.out.println("receiver = "+userId+" // noti = "+dto);
        notificationService.send(Long.parseLong(userId), dto.getReceiver(), dto.getType());
    }

    @GetMapping("/{userId}")
    public Integer findAllUnreadNotificationByUserId(@PathVariable("userId")String userId){
        return notificationService.findAllUnreadNotificationByUserId(Long.parseLong(userId)).size();
    }

    @GetMapping("/list/{userId}")
    public List<Notification> findAllNotificationByUserId(@PathVariable("userId") String userId){
        return notificationService.findAllNotificationByUserId(Long.parseLong(userId));
    }


}
