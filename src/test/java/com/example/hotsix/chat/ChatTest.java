package com.example.hotsix.chat;

import com.example.hotsix.dto.chat.ChatRoom;
import com.example.hotsix.dto.chat.ChatRoomStatus;
import com.example.hotsix.dto.chat.QChatRoom;
import com.example.hotsix.repository.chat.ChatRoomRepository;
import com.example.hotsix.service.chat.ChatRoomService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ChatTest {
    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    JPAQueryFactory factory;

    @Test
    public void insertTest(){
        String userA = "ssafy";
        String userB = "lim";
        String chatroomName = "새 채팅방";
        chatRoomService.createChatRoom(chatroomName, userA, userB);
        QChatRoom room = QChatRoom.chatRoom;
        ChatRoom chatroom = factory.selectFrom(room)
                .where(room.name.eq(chatroomName))
                .fetchOne();
        Assert.assertEquals(chatroomName, chatroom.getName());
    }
}
