package com.example.hotsix.vo;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@EqualsAndHashCode
public class ChatMessageVo {
    private String chatroomId;
    private String chatroomName;
    private String sender;
    private String receiver;
    private String content;
    private String sendDate;
    private Long descSendDate;
    private Boolean receiverStatus;

    @Getter
    private String type;
    public enum MessageType{
        ENTER, TALK, QUIT
    }


    @DynamoDbPartitionKey
    @DynamoDbAttribute("chatroom_id")
    public String getChatroomId() {
        return chatroomId;
    }

    @DynamoDbAttribute("chatroom_name")
    public String getChatroomName() {
        return chatroomName;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "SenderIndex")
    @DynamoDbAttribute("sender")
    public String getSender() {
        return sender;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "ReceiverIndex")
    @DynamoDbAttribute("receiver")
    public String getReceiver() {
        return receiver;
    }

    @DynamoDbAttribute("content")
    public String getContent() {
        return content;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("desc_send_date")
    public Long getDescSendDate() {
        return descSendDate;
    }

    @DynamoDbAttribute("send_date")
    public String getSendDate() {
        return sendDate;
    }

    public Boolean getReceiverStatus() {
        return this.receiverStatus;
    }
    public void setReceiverStatus(Boolean receiverStatus) {
        this.receiverStatus = receiverStatus;
    }

    @Override
    public String toString() {
        return "ChatMessageVo{" +
                "chatroomId='" + chatroomId + '\'' +
                ", chatroomName='" + chatroomName + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", content='" + content + '\'' +
                ", sendDate='" + sendDate + '\'' +
                ", descSendDate='" + descSendDate + '\'' +
                ", receiverStatus='" + receiverStatus + '\'' +
                "}\n";
    }
}
