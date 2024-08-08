package com.example.hotsix.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomStatusDto {
    private Long id;
    private Long chatRoomId;
    private Long userId;
    private Integer unreadCount;
    private Boolean online;
}