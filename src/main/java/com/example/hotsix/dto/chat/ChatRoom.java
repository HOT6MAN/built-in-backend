package com.example.hotsix.dto.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String name;
    private String create_date;
    private String last_message;
    private String last_message_date;

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", create_date='" + create_date + '\'' +
                ", last_message='" + last_message + '\'' +
                ", last_message_date='" + last_message_date + '\'' +
                "}\n";
    }
}
