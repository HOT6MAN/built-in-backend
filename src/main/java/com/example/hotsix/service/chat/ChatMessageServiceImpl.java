package com.example.hotsix.service.chat;

import com.example.hotsix.exception.chat.ChatMessageInsertException;
import com.example.hotsix.exception.chat.ChatMessageQueryException;
import com.example.hotsix.repository.chat.ChatMessageRepository;
import com.example.hotsix.vo.ChatMessageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

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


    @Override
    public ArrayList<ChatMessageVo> getUserChatRooms(String userId) {
        // Sender로 조회
        List<ChatMessageVo> senderMessages = chatMessageRepository.findBySender(userId);
        // Receiver로 조회
        List<ChatMessageVo> receiverMessages = chatMessageRepository.findByReceiver(userId);
        // 두 리스트를 합침
        List<ChatMessageVo> allMessages = new ArrayList<>(senderMessages);
        allMessages.addAll(receiverMessages);

        // 각 채팅방별로 가장 최근 메시지를 찾음
        Map<String, ChatMessageVo> latestMessagesByChatroom = allMessages.stream()
                .collect(Collectors.toMap(
                        ChatMessageVo::getChatroomId,
                        message -> message,
                        (existing, replacement) -> existing.getDescSendDate() > replacement.getDescSendDate() ? existing : replacement
                ));

        // 결과를 ArrayList로 반환
        ArrayList<ChatMessageVo> result = new ArrayList<>(latestMessagesByChatroom.values());
        result.sort(Comparator.comparingLong(ChatMessageVo::getDescSendDate));
        return result;
    }

}
