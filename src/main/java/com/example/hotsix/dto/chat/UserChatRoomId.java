package com.example.hotsix.dto.chat;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChatRoomId implements Serializable {
    private String userId;
    private String chatRoomId;

}