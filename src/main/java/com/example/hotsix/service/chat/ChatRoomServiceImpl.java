package com.example.hotsix.service.chat;

import com.example.hotsix.dto.chat.ChatRoom;
import com.example.hotsix.dto.chat.ChatRoomStatus;
import com.example.hotsix.dto.chat.UserChatRoomId;
import com.example.hotsix.repository.chat.ChatRoomRepository;
import com.example.hotsix.repository.chat.ChatRoomStatusRepository;
import com.example.hotsix.util.LocalTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService{
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomStatusRepository chatRoomStatusRepository;


    @Override
    @Transactional
    public ChatRoom createChatRoom(String chatRoomName, Long userAId, Long userBId) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomName)
                .create_date(LocalTimeUtil.getDateTime())
                .last_message(null)
                .last_message_date(null)
                .build();
        chatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomStatus userAStatus = ChatRoomStatus.builder()
                .chatRoom(chatRoom)
                .userId(userAId)
                .unreadCount(0)
                .online(false)
                .build();
        chatRoomStatusRepository.save(userAStatus);

        ChatRoomStatus userBStatus = ChatRoomStatus.builder()
                .chatRoom(chatRoom)
                .unreadCount(0)
                .online(false)
                .userId(userBId)
                .build();
        chatRoomStatusRepository.save(userBStatus);

        return chatRoom;
    }

    @Override
    public List<ChatRoomStatus> findAllChatRoomByUserId(Long userId){
        return chatRoomRepository.findAllChatRoomsByUserId(userId);
    }

    @Override
    public ChatRoomStatus findReceiver(Long chatroomId, Long userId){
        return chatRoomRepository.findReceiver(chatroomId, userId);
    }

    @Override
    public ChatRoomStatus findUser(Long chatroomId, Long userId) {
        return chatRoomRepository.findUser(chatroomId, userId);
    }

    @Override
    @Transactional
    public void updateOnlineStatus(Long chatroomId, Long userId) {
        chatRoomRepository.updateOnlineStatus(chatroomId, userId);
    }

    @Override
    @Transactional
    public void updateOfflineStatus(Long chatroomId, Long userId) {
        chatRoomRepository.updateOfflineStatus(chatroomId, userId);
    }

    @Override
    @Transactional
    public  void updateUnreadCount(Long chatroomId, Long userId){
        chatRoomRepository.updateUnreadCount(chatroomId, userId);
    }

    @Override
    @Transactional
    public void updateUnreadCount(Long chatroomId, Long userId, Integer num) {
        chatRoomRepository.updateUnreadCount(chatroomId, userId, num);
    }
}
