package com.example.hotsix.service.chat;

import com.example.hotsix.dto.chat.ChatRoom;
import com.example.hotsix.dto.chat.ChatRoomStatus;

import java.util.List;

public interface ChatRoomService {
    ChatRoom createChatRoom(Long boardId, Long memberId);
    List<ChatRoomStatus>   findAllChatRoomByUserId(Long userId);
    ChatRoomStatus findReceiver(Long chatroomId, Long userId);
    ChatRoomStatus findUser(Long chatroomId, Long userId);
    void updateOnlineStatus(Long chatroomId, Long userId);
    void updateOfflineStatus(Long chatroomId, Long userId);
    void updateUnreadCount(Long chatroomId, Long userId);
    void updateUnreadCount(Long chatroomId, Long userId, Integer num);

}
