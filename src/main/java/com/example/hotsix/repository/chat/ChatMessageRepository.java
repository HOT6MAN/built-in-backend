package com.example.hotsix.repository.chat;

import com.example.hotsix.exception.chat.ChatMessageQueryException;
import com.example.hotsix.util.LocalTimeUtil;
import com.example.hotsix.vo.ChatMessageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.*;

@Repository
public class ChatMessageRepository {
    private final DynamoDbTable<ChatMessageVo> table;
    private final DynamoDbIndex<ChatMessageVo> senderIndex;
    private final DynamoDbIndex<ChatMessageVo> receiverIndex;
    private final DynamoDbIndex<ChatMessageVo> receiverDateIndex;
    private final DynamoDbIndex<ChatMessageVo> senderDateIndex;

    public ChatMessageRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient){
        this.table = dynamoDbEnhancedClient.table("chatroom", TableSchema.fromBean(ChatMessageVo.class));
        this.senderIndex = table.index("SenderIndex");
        this.receiverIndex = table.index("ReceiverIndex");
        this.receiverDateIndex = table.index("ReceiverDateIndex");
        this.senderDateIndex = table.index("SenderDateIndex");
    }


    /**
     *
     * @param chatMessageVo
     * @feature DynamoDb에 data insert
     */
    public void insert(ChatMessageVo chatMessageVo){
        table.putItem(chatMessageVo);
    }

    /**
     *
     * @param date
     * @return List<ChatMessageVo>
     * @Feature 특정 기간 이전의 메시지들을 모두 찾는 메서드
     *          예를들어 LocalTimeUtil 클래스의 Instant 기준을 -30일을 하면
     *          현재 시간-30일 기준 그 이전 날짜인 메시지들을 모두 들고오는 것임.
     */
    public List<ChatMessageVo> findMessagesBeforeDate(String date) {
        try {
            Map<String, AttributeValue> whereDateValue = new HashMap<>();
            whereDateValue.put(":date", AttributeValue.builder().s(date).build());

            Map<String, String> whereDateName = new HashMap<>();
            whereDateName.put("#send_date", "send_date");
            Expression filterExpression = Expression.builder()
                    .expression("#send_date < :date")
                    .expressionValues(whereDateValue)
                    .expressionNames(whereDateName)
                    .build();

            ScanEnhancedRequest scan = ScanEnhancedRequest.builder()
                    .filterExpression(filterExpression)
                    .build();

            Iterator<ChatMessageVo> results = table.scan(scan).items().iterator();
            List<ChatMessageVo> messages = new ArrayList<>();
            while (results.hasNext()) {
                messages.add(results.next());
            }
            return messages;
        } catch (DynamoDbException e) {
            throw new ChatMessageQueryException("fail select", e);
        }
    }

    /**
     * @Feature 현재시간 -30일 이전 메시지들을 전부 삭제하는 메서드
     */
    public void deleteMessagesBeforeNow() {
        String standardDate = LocalTimeUtil.getDateTime();
        List<ChatMessageVo> messages = findMessagesBeforeDate(standardDate);
        for (ChatMessageVo message : messages) {
            table.deleteItem(message);
        }
    }


    /**
     *
     * @param chatroomId
     * @param date
     * @return List<ChatMessageVo>
     * @throws ChatMessageQueryException
     * @Feature Date와 ChatroomId 기준으로 메시지를 가져오는 메서드
     */
    public List<ChatMessageVo> findByChatroomIdAndBeforeDate(String chatroomId, String date)throws ChatMessageQueryException {
        try{
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(k -> k.partitionValue(chatroomId).sortValue(date));
            Iterator<ChatMessageVo> results = table.query(queryConditional).items().iterator();
            List<ChatMessageVo> messages = new ArrayList<>();
            while (results.hasNext()) {
                messages.add(results.next());
            }
            return messages;
        }
        catch(DynamoDbException e){
            throw new ChatMessageQueryException(e.getMessage());
        }
    }

    /**
     *
     * @param chatroomId
     * @return List<ChatMessageVo>
     * @throws ChatMessageQueryException
     * @Feature ChatroomId 기반으로 (PK) 모든 메시지를 가져오는 메서드
     *          즉, 방을 기준으로 해당 방의 모든 메시지를 가져오는 것임.
     */
    public List<ChatMessageVo> findByChatroomId(String chatroomId)throws ChatMessageQueryException {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(chatroomId));
            Iterator<ChatMessageVo> results = table.query(queryConditional).items().iterator();
            List<ChatMessageVo> messages = new ArrayList<>();
            while (results.hasNext()) {
                messages.add(results.next());
            }
            return messages;
        } catch (DynamoDbException e) {
            throw new ChatMessageQueryException("fail select", e);
        }
    }

}
