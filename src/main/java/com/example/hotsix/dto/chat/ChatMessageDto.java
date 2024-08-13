package com.example.hotsix.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    private String chatroomId;
    private String chatroomName;
    private String sender;
    private String receiver;
    private String senderName;
    private String receiverName;
    private String senderNickname;
    private String receiverNickname;
    private String content;
    private String sendDate;
    private String type;
    private Long descSendDate;
    private Boolean receiverStatus;
}
