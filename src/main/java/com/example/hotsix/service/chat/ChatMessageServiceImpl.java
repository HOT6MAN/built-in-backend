package com.example.hotsix.service.chat;

import com.example.hotsix.exception.chat.ChatMessageInsertException;
import com.example.hotsix.exception.chat.ChatMessageQueryException;
import com.example.hotsix.repository.chat.ChatMessageRepository;
import com.example.hotsix.vo.ChatMessageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public void insert(ChatMessageVo chatMessageVo) {
        try{
            chatMessageRepository.insert(chatMessageVo);
        }
        catch(Exception e){
            e.printStackTrace();

            throw new ChatMessageInsertException("Failed to insert chat message", e);
        }
    }

    @Override
    public List<ChatMessageVo> findByChatroomIdAndBeforeDate(String chatroomId, String date) {
        try {
            return chatMessageRepository.findByChatroomIdAndBeforeDate(chatroomId, date);
        } catch (ChatMessageQueryException e) {
            System.err.println("Error occurred while fetching chat messages: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<ChatMessageVo> findByChatroomId(String chatroomId) {
        try{
            List<ChatMessageVo> list = chatMessageRepository.findByChatroomId(chatroomId);
            return list;
        }
        catch(ChatMessageQueryException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public void deleteMessagesBeforeNow() {
        chatMessageRepository.deleteMessagesBeforeNow();
    }
}
