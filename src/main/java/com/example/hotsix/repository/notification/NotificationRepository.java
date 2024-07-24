package com.example.hotsix.repository.notification;

import com.example.hotsix.dto.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
}
