package com.example.hotsix.controller.chat;

import com.example.hotsix.dto.chat.ChatMessageDto;
import com.example.hotsix.dto.chat.ChatRoom;
import com.example.hotsix.dto.chat.ChatRoomStatus;
import com.example.hotsix.model.Board;
import com.example.hotsix.repository.chat.BoardRepository;
import com.example.hotsix.service.chat.ChatMessageService;
import com.example.hotsix.service.chat.ChatRoomService;
import com.example.hotsix.vo.ChatMessageVo;
import com.example.hotsix.vo.ChatRoomVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    /**
     *
     * @param userId
     * @return ResponseEntity
     * @Feature 사용자가 속해있는 모든 채팅방을 가져온다.
     * 이때, 가장 마지막으로 사용한 메시지를 같이 보내야한다.
     * 카카오톡의 채팅방 목록을 생각하면 될듯.
     */
    @GetMapping("/list/{userId}")
    public List<ChatRoomStatus> findAllRoomByUserId(@PathVariable String userId){
        List<ChatRoomStatus> list = chatRoomService.findAllChatRoomByUserId(Long.parseLong(userId));
        log.info("find All Room By User Id = {}", list);
        return list;
    }

    /**
     *
     * @param roomId
     * @return ResponseEntity
     * @Feature 특정 채팅방의 모든 메시지를 불러온다.
     */
    @GetMapping("/room/{roomId}")
    public List<ChatMessageDto> findAllMessageByChatroomId(@PathVariable String roomId, String userId){
        List<ChatMessageDto> messages = chatMessageService.findChatMessageDtoByChatroomId(roomId, userId);
        Collections.reverse(messages);
        return messages;
    }

    @GetMapping("/receiver/{chatroomId}/{userId}")
    public Long findReceiver(@PathVariable("chatroomId")String chatroomId, @PathVariable("userId")String userId){
        return chatRoomService.findReceiver(Long.parseLong(chatroomId), Long.parseLong(userId)).getUserId();
    }



    @PostMapping("/room/{boardId}")
    public String createChatRoomByBoardId(@PathVariable("boardId")Long boardId, @RequestBody String userId){
        System.out.println("receive board Id = "+boardId + " // userId = "+userId);
        chatRoomService.createChatRoom(boardId, Long.parseLong(userId));
        return null;
    }


}
