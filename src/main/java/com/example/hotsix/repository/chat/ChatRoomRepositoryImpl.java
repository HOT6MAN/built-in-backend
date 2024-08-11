package com.example.hotsix.repository.chat;

import com.example.hotsix.dto.chat.ChatRoom;
import com.example.hotsix.dto.chat.ChatRoomStatus;
import com.example.hotsix.dto.chat.ChatRoomStatusDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.hotsix.dto.chat.QChatRoom.chatRoom;
import static com.example.hotsix.dto.chat.QChatRoomStatus.chatRoomStatus;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<ChatRoomStatus> findAllChatRoomsByUserId(Long userId) {
        List<ChatRoomStatus> statuses = queryFactory
                .selectFrom(chatRoomStatus)
                .join(chatRoomStatus.chatRoom, chatRoom).fetchJoin()
                .where(chatRoomStatus.userId.eq(userId))
                .fetch();

        return statuses;
    }

    @Override
    public Optional<ChatRoom> findChatRoomBetweenUsers(Long userAId, Long userBId){
        return Optional.ofNullable(
            queryFactory.selectFrom(chatRoom)
                    .join(chatRoomStatus).on(chatRoom.id.eq(chatRoomStatus.chatRoom.id))
                    .where(chatRoomStatus.userId.in(userAId, userBId))
                    .groupBy(chatRoom.id)
                    .having(chatRoomStatus.userId.countDistinct().eq(2L))
                    .fetchFirst()
        );
    }

    @Override
    public ChatRoomStatus findReceiver(Long chatroomId, Long userId){
        ChatRoomStatus receiver = queryFactory
                .selectFrom(chatRoomStatus)
                .where(
                        chatRoomStatus.chatRoom.id.eq(chatroomId)
                                .and(chatRoomStatus.userId.ne(userId))
                )
                .fetchOne();
        return receiver;
    }

    @Override
    public ChatRoomStatus findUser(Long chatroomId, Long userId) {
        ChatRoomStatus user = queryFactory
                .selectFrom(chatRoomStatus)
                .where(
                        chatRoomStatus.chatRoom.id.eq(chatroomId)
                                .and(chatRoomStatus.userId.eq(userId))
                ).fetchOne();
        return user;
    }

    @Override
    public void updateOnlineStatus(Long chatroomId, Long userId) {
        queryFactory.update(chatRoomStatus)
                .set(chatRoomStatus.online, true)
                .where(
                        chatRoomStatus.chatRoom.id.eq(chatroomId).and(
                                chatRoomStatus.userId.eq(userId)
                        )
                )
                .execute();
    }

    @Override
    public void updateOfflineStatus(Long chatroomId, Long userId) {
        queryFactory.update(chatRoomStatus)
                .set(chatRoomStatus.online, false)
                .where(
                        chatRoomStatus.chatRoom.id.eq(chatroomId).and(
                                chatRoomStatus.userId.eq(userId)
                        )
                )
                .execute();
    }

    @Override
    public void updateUnreadCount(Long chatroomId, Long userId) {
        queryFactory.update(chatRoomStatus)
                .set(chatRoomStatus.unreadCount, 0)
                .where(
                        chatRoomStatus.chatRoom.id.eq(chatroomId).and(
                                chatRoomStatus.userId.eq(userId)
                        )
                )
                .execute();
    }

    @Override
    public void updateUnreadCount(Long chatroomId, Long userId, Integer num) {
        queryFactory.update(chatRoomStatus)
                .set(chatRoomStatus.unreadCount, chatRoomStatus.unreadCount.add(num))
                .where(
                        chatRoomStatus.chatRoom.id.eq(chatroomId).and(
                                chatRoomStatus.userId.eq(userId)
                        )
                )
                .execute();
    }

}