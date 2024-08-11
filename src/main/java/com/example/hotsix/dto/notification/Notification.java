package com.example.hotsix.dto.notification;

import com.example.hotsix.model.NotificationDto;
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

    public NotificationDto toDto() {
        return NotificationDto.builder()
                .id(this.id)
                .receiver(this.receiver)
                .sender(this.sender)
                .type(this.type)
                .url(this.url)
                .isRead(this.isRead)
                .notifyDate(this.notifyDate)
                .build();
    }

    public static Notification fromDto(NotificationDto dto) {
        return Notification.builder()
                .id(dto.getId())
                .receiver(dto.getReceiver())
                .sender(dto.getSender())
                .type(dto.getType())
                .url(dto.getUrl())
                .isRead(dto.isRead())
                .notifyDate(dto.getNotifyDate())
                .build();
    }

    public void setProperties(NotificationDto dto) {
        this.receiver = dto.getReceiver();
        this.sender = dto.getSender();
        this.type = dto.getType();
        this.url = dto.getUrl();
        this.isRead = dto.isRead();
        this.notifyDate = dto.getNotifyDate();
    }
}
