package com.example.hotsix.service.chat;

import com.example.hotsix.vo.ChatMessageVo;
import com.example.hotsix.vo.ChatRoomVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface ChatMessageService {
    void insert(ChatMessageVo chatMessageVo);
    List<ChatMessageVo> findByChatroomIdAndBeforeDate(String chatroomId, String date);
    List<ChatMessageVo> findByChatroomId(String chatroomId, String userId);
    void deleteMessagesBeforeNow();
    ArrayList<ChatRoomVo> getUserChatRooms(String userId);
}
