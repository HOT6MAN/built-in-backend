package com.example.hotsix.dbtest;


import com.example.hotsix.repository.chat.ChatMessageRepository;
import com.example.hotsix.service.chat.ChatMessageService;
import com.example.hotsix.util.LocalTimeUtil;
import com.example.hotsix.vo.ChatMessageVo;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
public class DynamodbTest {
//    @Autowired
//    ChatMessageService service;
//    @Autowired
//    ChatMessageRepository repository;
//
//
//    @DisplayName("DynamoDB에 데이터를 삽입한다. NoSQL이기때문에 스키마 제약 없는 데이터가 삽입 가능하다.")
//    @Test
//    public void insertTest(){
//        ChatMessageVo chatMessageVo = null;
//        for(int i=0; i<10; i++){
//            chatMessageVo = ChatMessageVo.builder()
//                    .chatroomId("test_room_"+(100+i))
//                    .chatroomName("test")
//                    .sender("lim")
//                    .receiver("ssafy5")
//                    .sendDate(LocalTimeUtil.getDateTime())
//                    .descSendDate(LocalTimeUtil.getDescDateTime())
//                    .content("hello World"+i)
//                    .build();
//            service.insert(chatMessageVo);
//        }
//    }
//
//    @Test
//    void selectTest(){
//        System.out.println(service.getUserChatRooms("lim"));
//    }

//    @DisplayName("DynamoDB의 아이템을 Select한다." +
//            "Item은 PK인 chatroom_id를 토대로 가져온다." +
//            "chatroom_id 기준 어떤 사람들이 포함될 수 있는지 가져올 수 있기 때문에.")
//    @Test
//    public void selectByChatroomIdTest(){
//        List<ChatMessageVo>list = service.findByChatroomId("test_room_1", "ssafy");
//        List<ChatMessageVo>list2 = service.findByChatroomId("test_room_2", "ssafy");
//        Assert.assertEquals(2, list.size());
//        Assert.assertEquals(2, list2.size());
//    }

//    @DisplayName("일정 기간(ex)현재 기준 30일)이전 메시지를 삭제하는 메서드 테스트")
//    @Test
//    public void deleteChatMessageTest(){
//        List<ChatMessageVo> list = service.findByChatroomId("test_room_1", "ssafy");
//        List<ChatMessageVo> list2 = service.findByChatroomId("test_room_2", "ssafy");
//        Assert.assertEquals(2,list.size());
//        Assert.assertEquals(2,list2.size());
//        service.deleteMessagesBeforeNow();
//        list = service.findByChatroomId("test_room_1", "ssafy");
//        list2 = service.findByChatroomId("test_room_2", "ssafy");
//        Assert.assertEquals(0, list.size());
//        Assert.assertEquals(0, list2.size());
//    }
//
//    @Test
//    public void dateTest(){
//        System.out.println(LocalTimeUtil.getDateTime());
//        System.out.println(LocalTimeUtil.getMinusDateByNow());
//    }
}
