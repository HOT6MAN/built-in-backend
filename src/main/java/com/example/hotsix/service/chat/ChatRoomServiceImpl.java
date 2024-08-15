package com.example.hotsix.service.chat;

import com.example.hotsix.dto.chat.ChatRoom;
import com.example.hotsix.dto.chat.ChatRoomStatus;
import com.example.hotsix.dto.chat.UserChatRoomId;
import com.example.hotsix.dto.notification.Notification;
import com.example.hotsix.model.Member;
import com.example.hotsix.repository.chat.BoardRepository;
import com.example.hotsix.repository.chat.ChatRoomRepository;
import com.example.hotsix.repository.chat.ChatRoomStatusRepository;
import com.example.hotsix.repository.member.MemberRepository;
import com.example.hotsix.service.notification.NotificationService;
import com.example.hotsix.util.LocalTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService{
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomStatusRepository chatRoomStatusRepository;
    private final BoardRepository boardRepository;
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ChatRoom createChatRoom(Long boardId, Long memberId) {
        Long receiveId = boardRepository.findBoardByBoardId(boardId).getAuthor().getId();
        Optional<ChatRoom> chatroom = chatRoomRepository.findChatRoomBetweenUsers(memberId, receiveId);
        if(chatroom.isPresent()){
            System.out.println("Can't Create because room is already exist");
            return null;
        }
        ChatRoom chatRoom = ChatRoom.builder()
                .create_date(LocalTimeUtil.getDateTime())
                .last_message("새로운 채팅이 활성화 되었습니다!")
                .last_message_date(LocalTimeUtil.getDateTime())
                .build();
        chatRoom = chatRoomRepository.save(chatRoom);
        Member member = memberRepository.findMemberById(memberId);
        Member receiver = memberRepository.findMemberById(receiveId);
        String memberName = member.getName();
        String receiverName = receiver.getName();

        ChatRoomStatus userAStatus = ChatRoomStatus.builder()
                .chatRoom(chatRoom)
                .roomName(receiverName+"님과의 채팅방")
                .userId(memberId)
                .unreadCount(0)
                .online(false)
                .build();
        chatRoomStatusRepository.save(userAStatus);

        ChatRoomStatus userBStatus = ChatRoomStatus.builder()
                .chatRoom(chatRoom)
                .roomName(memberName+"님과의 채팅방")
                .unreadCount(0)
                .online(false)
                .userId(receiveId)
                .build();
        chatRoomStatusRepository.save(userBStatus);
        notificationService.send(member.getId(), receiver.getId(), "chat");
        notificationService.save(Notification.builder()
                .isRead(false)
                .url("/")
                .type("chat")
                .sender(memberId)
                .receiver(receiveId)
                .notifyDate(LocalTimeUtil.getDateTime())
                .build());
        System.out.println("Create ChatRoom and ChatRoom Status Successfully");
        return chatRoom;
    }

    @Override
    public List<ChatRoomStatus> findAllChatRoomByUserId(Long userId){
        List<ChatRoomStatus> list = chatRoomRepository.findAllChatRoomsByUserId(userId);
        for(ChatRoomStatus status : list){

        }
        return chatRoomRepository.findAllChatRoomsByUserId(userId);
    }

    @Override
    public ChatRoomStatus findReceiver(Long chatroomId, Long userId){
        return chatRoomRepository.findReceiver(chatroomId, userId);
    }

    @Override
    public ChatRoomStatus findUser(Long chatroomId, Long userId) {
        return chatRoomRepository.findUser(chatroomId, userId);
    }

    @Override
    @Transactional
    public void updateOnlineStatus(Long chatroomId, Long userId) {
        chatRoomRepository.updateOnlineStatus(chatroomId, userId);
    }

    @Override
    @Transactional
    public void updateOfflineStatus(Long chatroomId, Long userId) {
        chatRoomRepository.updateOfflineStatus(chatroomId, userId);
    }

    @Override
    @Transactional
    public  void updateUnreadCount(Long chatroomId, Long userId){
        chatRoomRepository.updateUnreadCount(chatroomId, userId);
    }

    @Override
    @Transactional
    public void updateUnreadCount(Long chatroomId, Long userId, Integer num) {
        chatRoomRepository.updateUnreadCount(chatroomId, userId, num);
    }

    public void updateLastMessage(Long chatRoomId, String message){
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        chatRoom.setLast_message(message);
        chatRoom.setLast_message_date(LocalTimeUtil.getDateTime());
        chatRoomRepository.save(chatRoom);
    }

}
