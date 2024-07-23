package com.example.hotsix.controller.chat;

import com.example.hotsix.service.chat.ChatMessageService;
import com.example.hotsix.vo.ChatMessageVo;
import com.example.hotsix.vo.ChatRoomVo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {
    private final ChatMessageService chatMessageService;
    /**
     *
     * @param userId
     * @param response
     * @return ResponseEntity
     * @Feature 사용자가 속해있는 모든 채팅방을 가져온다.
     * 이때, 가장 마지막으로 사용한 메시지를 같이 보내야한다.
     * 카카오톡의 채팅방 목록을 생각하면 될듯.
     */
    @GetMapping("/list/{userId}")
    public List<ChatRoomVo> sendChatList(@PathVariable String userId, HttpServletResponse response){
        List<ChatRoomVo> messages = chatMessageService.getUserChatRooms(userId);
        return messages;
    }

    /**
     *
     * @param roomId
     * @return ResponseEntity
     * @Feature 특정 채팅방의 모든 메시지를 불러온다.
     */
    @GetMapping("/room/{roomId}")
    public List<ChatMessageVo> findAllMessageByChatroomId(@PathVariable String roomId, String userId){
        List<ChatMessageVo> messages = chatMessageService.findByChatroomId(roomId, userId);
        Collections.reverse(messages);
        return messages;
    }



}
