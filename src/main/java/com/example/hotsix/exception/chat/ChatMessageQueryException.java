package com.example.hotsix.exception.chat;

public class ChatMessageQueryException extends RuntimeException{
    public ChatMessageQueryException(String message) {
        super(message);
    }

    public ChatMessageQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
