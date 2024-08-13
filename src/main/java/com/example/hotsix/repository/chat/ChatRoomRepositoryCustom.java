package com.example.hotsix.repository.chat;

import com.example.hotsix.dto.chat.ChatRoom;
import com.example.hotsix.dto.chat.ChatRoomStatus;
import com.example.hotsix.dto.chat.ChatRoomStatusDto;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepositoryCustom{
    ChatRoom findChatRoomById(Long chatRoomId);
    List<ChatRoomStatus>  findAllChatRoomsByUserId(Long userId);
    Optional<ChatRoom> findChatRoomBetweenUsers(Long userAId, Long userBId);
    ChatRoomStatus findReceiver(Long chatroomId, Long userId);
    ChatRoomStatus findUser(Long chatroomId, Long userId);
    void updateOnlineStatus(Long chatroomId, Long userId);
    void updateOfflineStatus(Long chatroomId, Long userId);
    void updateUnreadCount(Long chatroomId, Long userId);
    void updateUnreadCount(Long chatroomId, Long userId, Integer num);
}
