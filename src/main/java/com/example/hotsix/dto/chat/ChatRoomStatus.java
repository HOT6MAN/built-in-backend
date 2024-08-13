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

    @Column(name = "room_name")
    private String roomName;

    private Long userId;

    private Integer unreadCount;

    private Boolean online;

    public ChatRoomStatusDto toDto() {
        return ChatRoomStatusDto.builder()
                .id(this.id)
                .chatRoomId(this.chatRoom != null ? this.chatRoom.getId() : null)
                .userId(this.userId)
                .unreadCount(this.unreadCount)
                .online(this.online)
                .build();
    }

    public static ChatRoomStatus fromDto(ChatRoomStatusDto dto) {
        return ChatRoomStatus.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .unreadCount(dto.getUnreadCount())
                .online(dto.getOnline())
                .build();
    }

    public void setProperties(ChatRoomStatusDto dto) {
        this.userId = dto.getUserId();
        this.unreadCount = dto.getUnreadCount();
        this.online = dto.getOnline();
    }
    @Override
    public String toString() {
        return "ChatRoomStatus{" +
                "id=" + id +
                "ChatRoom=" + chatRoom +
                ", userId=" + userId +
                ", unreadCount=" + unreadCount +
                ", online=" + online +
                "}\n";
    }
}
