package com.example.hotsix.controller.notification;

import com.example.hotsix.dto.notification.Notification;
import com.example.hotsix.model.NotificationDto;
import com.example.hotsix.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notify")
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable String userId,
                                @RequestHeader(value="Last-Event-Id", required = false, defaultValue = "")String lastEventId) {
        log.info("call subscribe And Subscribe Id = {}", userId);
        return notificationService.subscribe(userId, lastEventId);
    }

    @PostMapping("/{userId}")
    public void notify(@PathVariable("userId") String userId, @RequestBody Notification dto){
        System.out.println("receiver = "+userId+" // noti = "+dto);
        notificationService.send(Long.parseLong(userId), dto.getReceiver(), dto.getType());
    }

    @GetMapping("/{userId}")
    public Integer findAllUnreadNotificationByUserId(@PathVariable("userId")String userId){
        log.info("find All Unread Notification By User Id Call // user Id = {}", userId);
        List<Notification> list = notificationService.findAllNotificationByUserId(Long.parseLong(userId));
        log.info("unread Notification = {}",list);
        return notificationService.findAllUnreadNotificationByUserId(Long.parseLong(userId)).size();
    }

    @GetMapping("/list/{userId}")
    public List<NotificationDto> findAllNotificationByUserId(@PathVariable("userId") String userId){
        log.info("find All Notification By User Id Called");
        List<NotificationDto> list = notificationService.findAllNotificationDtoByUserId(Long.parseLong(userId));
        log.info("find Notification Result = {}", list);
        return list;
    }


}
