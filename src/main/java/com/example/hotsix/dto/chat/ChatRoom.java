package com.example.hotsix.dto.chat;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "chatroom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "create_date")
    private String create_date;
    @Column(name = "last_message")
    private String last_message;
    @Column(name = "last_message_date")
    private String last_message_date;

    public ChatRoomDto toDto() {
        return ChatRoomDto.builder()
                .id(this.id)
                .create_date(this.create_date)
                .last_message(this.last_message)
                .last_message_date(this.last_message_date)
                .build();
    }

    public static ChatRoom fromDto(ChatRoomDto dto) {
        return ChatRoom.builder()
                .id(dto.getId())
                .create_date(dto.getCreate_date())
                .last_message(dto.getLast_message())
                .last_message_date(dto.getLast_message_date())
                .build();
    }

    public void setProperties(ChatRoomDto dto) {
        this.create_date = dto.getCreate_date();
        this.last_message = dto.getLast_message();
        this.last_message_date = dto.getLast_message_date();
    }
    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", create_date='" + create_date + '\'' +
                ", last_message='" + last_message + '\'' +
                ", last_message_date='" + last_message_date + '\'' +
                "}\n";
    }
}
