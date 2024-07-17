package com.example.hotsix.exception.chat;

public class ChatMessageInsertException extends RuntimeException{
    public ChatMessageInsertException(String message) {
        super(message);
    }

    public ChatMessageInsertException(String message, Throwable cause) {
        super(message, cause);
    }
}
