package com.example.hotsix.repository.chat;

import com.example.hotsix.exception.chat.ChatMessageQueryException;
import com.example.hotsix.util.LocalTimeUtil;
import com.example.hotsix.vo.ChatMessageVo;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.*;

@Repository
public class ChatMessageRepository {
    private final DynamoDbTable<ChatMessageVo> table;
    private final DynamoDbIndex<ChatMessageVo> senderIndex;
    private final DynamoDbIndex<ChatMessageVo> receiverIndex;

    public ChatMessageRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.table = dynamoDbEnhancedClient.table("chatroom", TableSchema.fromBean(ChatMessageVo.class));
        this.senderIndex = this.table.index("SenderIndex");
        this.receiverIndex = this.table.index("ReceiverIndex");
    }

    public List<ChatMessageVo> findBySender(String sender) {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(sender));
            SdkIterable<Page<ChatMessageVo>> results = senderIndex.query(queryConditional);

            List<ChatMessageVo> messages = new ArrayList<>();
            for (Page<ChatMessageVo> page : results) {
                messages.addAll(page.items());
            }
            return messages;
        } catch (DynamoDbException e) {
            throw new ChatMessageQueryException("Failed to query by sender", e);
        }
    }

    public List<ChatMessageVo> findByReceiver(String receiver) {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(receiver));
            SdkIterable<Page<ChatMessageVo>> results = receiverIndex.query(queryConditional);

            List<ChatMessageVo> messages = new ArrayList<>();
            for (Page<ChatMessageVo> page : results) {
                messages.addAll(page.items());
            }
            return messages;
        } catch (DynamoDbException e) {
            throw new ChatMessageQueryException("Failed to query by receiver", e);
        }
    }

    public void insert(ChatMessageVo chatMessageVo) {
        table.putItem(chatMessageVo);
    }

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

    public void deleteMessagesBeforeNow() {
        String standardDate = LocalTimeUtil.getDateTime();
        List<ChatMessageVo> messages = findMessagesBeforeDate(standardDate);
        for (ChatMessageVo message : messages) {
            table.deleteItem(message);
        }
    }

    public List<ChatMessageVo> findByChatroomIdAndBeforeDate(String chatroomId, String date) throws ChatMessageQueryException {
        try {
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(k -> k.partitionValue(chatroomId).sortValue(date));
            Iterator<ChatMessageVo> results = table.query(queryConditional).items().iterator();
            List<ChatMessageVo> messages = new ArrayList<>();
            while (results.hasNext()) {
                messages.add(results.next());
            }
            return messages;
        } catch (DynamoDbException e) {
            throw new ChatMessageQueryException(e.getMessage());
        }
    }

    public List<ChatMessageVo> findByChatroomId(String chatroomId) throws ChatMessageQueryException {
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
