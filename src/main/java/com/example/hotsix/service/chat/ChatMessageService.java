package com.example.hotsix.service.chat;

import com.example.hotsix.vo.ChatMessageVo;

import java.util.List;

public interface ChatMessageService {
    void insert(ChatMessageVo chatMessageVo);
    List<ChatMessageVo> findByChatroomIdAndBeforeDate(String chatroomId, String date);
    List<ChatMessageVo> findByChatroomId(String chatroomId);
    void deleteMessagesBeforeNow();
}
