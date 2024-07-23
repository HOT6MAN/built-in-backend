package com.example.hotsix.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NotificationDto {
    private String receiver;
    private String notificationType;
    private String content;
    private String url;
    private boolean isRead;

    @Override
    public String toString() {
        return "NotificationDto{" +
                "receiver='" + receiver + '\'' +
                ", notificationType='" + notificationType + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", isRead=" + isRead +
                "}\n";
    }
}
