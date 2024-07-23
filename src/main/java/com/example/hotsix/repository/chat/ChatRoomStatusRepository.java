package com.example.hotsix.repository.chat;

import com.example.hotsix.dto.chat.ChatRoom;
import com.example.hotsix.dto.chat.ChatRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomStatusRepository extends JpaRepository<ChatRoomStatus, Long> {
    ChatRoomStatus findByChatRoomAndUserId(ChatRoom chatRoom, String userId);
}
