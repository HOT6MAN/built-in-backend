package com.example.hotsix.vo;

import com.example.hotsix.dto.chat.ChatMessageDto;
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
    public ChatMessageDto toDto() {
        return ChatMessageDto.builder()
                .chatroomId(this.chatroomId)
                .chatroomName(this.chatroomName)
                .sender(this.sender)
                .receiver(this.receiver)
                .content(this.content)
                .sendDate(this.sendDate)
                .descSendDate(this.descSendDate)
                .receiverStatus(this.receiverStatus)
                .type(this.type)
                .build();
    }

    // Method to populate VO from DTO
    public static ChatMessageVo fromDto(ChatMessageDto dto) {
        return ChatMessageVo.builder()
                .chatroomId(dto.getChatroomId())
                .chatroomName(dto.getChatroomName())
                .sender(dto.getSender())
                .receiver(dto.getReceiver())
                .content(dto.getContent())
                .sendDate(dto.getSendDate())
                .descSendDate(dto.getDescSendDate())
                .receiverStatus(dto.getReceiverStatus())
                .type(dto.getType())
                .build();
    }

    // Method to set properties from DTO
    public void setProperties(ChatMessageDto dto) {
        this.chatroomId = dto.getChatroomId();
        this.chatroomName = dto.getChatroomName();
        this.sender = dto.getSender();
        this.receiver = dto.getReceiver();
        this.content = dto.getContent();
        this.sendDate = dto.getSendDate();
        this.descSendDate = dto.getDescSendDate();
        this.receiverStatus = dto.getReceiverStatus();
        this.type = dto.getType();
    }
}
