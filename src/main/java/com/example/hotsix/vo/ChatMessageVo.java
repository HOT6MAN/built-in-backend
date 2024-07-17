package com.example.hotsix.vo;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class ChatMessageVo {
    private String chatroomId;
    private String chatroomName;
    private String sender;
    private String receiver;
    private String content;
    private String date;
    private String dateForReceiverIndex;
    private String dateForSenderIndex;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("chatroom_id")
    public String getChatroomId() {
        return chatroomId;
    }
    @DynamoDbAttribute("chatroomName")
    public String getChatroomName() {
        return chatroomName;
    }
    @DynamoDbSecondarySortKey(indexNames = "SenderIndex")
    @DynamoDbAttribute("sender")
    public String getSender() {
        return sender;
    }
    @DynamoDbSecondarySortKey(indexNames = "ReceiverIndex")
    @DynamoDbAttribute("receiver")
    public String getReceiver() {
        return receiver;
    }
    @DynamoDbAttribute("content")
    public String getContent() {
        return content;
    }
    @DynamoDbSortKey
    @DynamoDbAttribute("send_date")
    public String getDate() {
        return date;
    }

    @DynamoDbSecondarySortKey(indexNames = {"ReceiverDateIndex"})
    @DynamoDbAttribute("dateForReceiverIndex")
    public String getDateForReceiverIndex() {
        return this.dateForReceiverIndex;
    }
    @DynamoDbSecondarySortKey(indexNames = {"SenderDateIndex"})
    @DynamoDbAttribute("dateForSenderIndex")
    public String getDateForSenderIndex(){
        return this.dateForSenderIndex;
    }
}
