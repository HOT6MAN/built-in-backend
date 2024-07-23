package com.example.hotsix.vo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomVo {
    private String chatroomId;
    private ChatMessageVo latestMessage;
    private long unreadCount;

    public ChatRoomVo(String chatroomId, ChatMessageVo latestMessage, long unreadCount) {
        this.chatroomId = chatroomId;
        this.latestMessage = latestMessage;
        this.unreadCount = unreadCount;
    }

    @Override
    public String toString() {
        return "ChatRoomVo{" +
                "chatroomId='" + chatroomId + '\'' +
                ", latestMessage=" + latestMessage +
                ", unreadCount=" + unreadCount +
                "}\n";
    }
}
