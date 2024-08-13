package com.example.hotsix.model;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private Long id;
    private Long receiver;
    private Long sender;
    private String receiverName;
    private String senderName;
    private String content;
    private String type;
    private String url;
    private boolean isRead;
    private String notifyDate;
}
