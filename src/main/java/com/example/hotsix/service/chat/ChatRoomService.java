package com.example.hotsix.service.chat;

import com.example.hotsix.dto.chat.ChatRoom;

public interface ChatRoomService {
    ChatRoom createChatRoom(String chatRoomName, String userAId, String userBId);
}
