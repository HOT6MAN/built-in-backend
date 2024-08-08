package com.example.hotsix.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDto {
    private Long id;
    private String name;
    private String create_date;
    private String last_message;
    private String last_message_date;
}