package com.example.hotsix.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomStatusDto extends ChatRoomStatus{
    private String receiver;
}
