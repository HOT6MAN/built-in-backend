package com.example.hotsix.service.chat;

import com.example.hotsix.dto.chat.ChatRoom;
import com.example.hotsix.dto.chat.ChatRoomStatus;
import com.example.hotsix.dto.chat.UserChatRoomId;
import com.example.hotsix.repository.chat.ChatRoomRepository;
import com.example.hotsix.repository.chat.ChatRoomStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService{
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomStatusRepository chatRoomStatusRepository;


    @Override
    @Transactional
    public ChatRoom createChatRoom(String chatRoomName, String userAId, String userBId) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomName)
                .build();
        chatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomStatus userAStatus = ChatRoomStatus.builder()
                .chatRoom(chatRoom)
                .userId(userAId)
                .build();
        chatRoomStatusRepository.save(userAStatus);

        ChatRoomStatus userBStatus = ChatRoomStatus.builder()
                .chatRoom(chatRoom)
                .userId(userBId)
                .build();
        chatRoomStatusRepository.save(userAStatus);

        return chatRoom;
    }
}
