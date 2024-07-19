package com.example.hotsix.controller.chat;

import com.example.hotsix.service.chat.ChatMessageService;
import com.example.hotsix.vo.ChatMessageVo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> sendChatList(@PathVariable String userId, HttpServletResponse response){
        List<ChatMessageVo> messages = chatMessageService.getUserChatRooms(userId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    /**
     *
     * @param roomId
     * @param response
     * @return ResponseEntity
     * @Feature 특정 채팅방의 모든 메시지를 불러온다.
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> findAllMessageByChatroomId(@PathVariable String roomId, HttpServletResponse response){
        log.error("call detail Messages");
        List<ChatMessageVo> messages = chatMessageService.findByChatroomId(roomId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }


}
