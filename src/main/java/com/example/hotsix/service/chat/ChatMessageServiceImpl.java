package com.example.hotsix.service.chat;

import com.example.hotsix.exception.chat.ChatMessageInsertException;
import com.example.hotsix.exception.chat.ChatMessageQueryException;
import com.example.hotsix.repository.chat.ChatMessageRepository;
import com.example.hotsix.repository.chat.ChatRoomRepository;
import com.example.hotsix.vo.ChatMessageVo;
import com.example.hotsix.vo.ChatRoomVo;
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
    private final ChatRoomRepository chatRoomRepository;

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
    public List<ChatMessageVo> findChatMessageByChatroomId(String chatroomId, String userId) {
        try{
            List<ChatMessageVo> list = chatMessageRepository.findChatMessageByChatroomId(chatroomId, userId);
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
    public ArrayList<ChatRoomVo> getUserChatRooms(String userId) {
        // Sender와 Receiver 메시지 목록을 각각 가져옴
        List<ChatMessageVo> senderMessages = chatMessageRepository.findBySender(userId);
        List<ChatMessageVo> receiverMessages = chatMessageRepository.findByReceiver(userId);

        // 모든 메시지를 합침 (중복 제거)
        List<ChatMessageVo> allMessages = new ArrayList<>(senderMessages);
        allMessages.addAll(receiverMessages);

        // 메시지를 채팅방 ID로 그룹화
        Map<String, List<ChatMessageVo>> messagesByChatroom = allMessages.stream()
                .collect(Collectors.groupingBy(ChatMessageVo::getChatroomId));

        // 디버그: 메시지 그룹화 결과 출력
        System.out.println("map = " + messagesByChatroom);

        ArrayList<ChatRoomVo> result = new ArrayList<>();

        for (Map.Entry<String, List<ChatMessageVo>> entry : messagesByChatroom.entrySet()) {
            String chatroomId = entry.getKey();
            List<ChatMessageVo> messages = entry.getValue();

            // 채팅방에서 Desc_send_date가 가장 작은 메시지를 가져옴
            ChatMessageVo latestMessage = messages.stream()
                    .min(Comparator.comparingLong(ChatMessageVo::getDescSendDate))
                    .orElse(null);

        }

        // 채팅방을 Desc_send_date 값이 가장 작은 메시지를 기준으로 정렬
        result.sort(Comparator.comparingLong(chatRoom -> chatRoom.getLatestMessage().getDescSendDate()));
        // 디버그: 최종 결과 출력
        System.out.println("result = " + result);
        return result;
    }






}
