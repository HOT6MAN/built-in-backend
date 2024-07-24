package com.example.hotsix.dto.chat;

import jakarta.persistence.*;
import lombok.*;

@Entity(name="chatroom_status")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChatRoomStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatRoom;

    private Long userId;

    private Integer unreadCount;

    private Boolean online;

    @Override
    public String toString() {
        return "ChatRoomStatus{" +
                "id=" + id +
                ", userId=" + userId +
                ", unreadCount=" + unreadCount +
                ", online=" + online +
                "}\n";
    }
}
