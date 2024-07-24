package com.example.hotsix.dto.notification;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long receiver;
    private Long sender;
    private String type;
    private String url;
    @Column(name = "is_read")
    private boolean isRead;
    @Column(name = "notify_date")
    private String notifyDate;

    @Override
    public String toString() {
        return "NotificationDto{" +
                "receiver='" + receiver + '\'' +
                "sender='" + sender + '\'' +
                ", notificationType='" + type + '\'' +
                ", url='" + url + '\'' +
                ", isRead=" + isRead +
                "}\n";
    }
}
