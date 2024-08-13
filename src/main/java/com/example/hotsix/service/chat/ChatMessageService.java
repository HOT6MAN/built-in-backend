package com.example.hotsix.service.chat;

import com.example.hotsix.dto.chat.ChatMessageDto;
import com.example.hotsix.vo.ChatMessageVo;
import com.example.hotsix.vo.ChatRoomVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface ChatMessageService {
    void insert(ChatMessageVo chatMessageVo);
    List<ChatMessageVo> findByChatroomIdAndBeforeDate(String chatroomId, String date);
    List<ChatMessageVo> findChatMessageByChatroomId(String chatroomId, String userId);
    List<ChatMessageDto> findChatMessageDtoByChatroomId(String chatroomId, String userId);
    void deleteMessagesBeforeNow();
    ArrayList<ChatRoomVo> getUserChatRooms(String userId);
}
