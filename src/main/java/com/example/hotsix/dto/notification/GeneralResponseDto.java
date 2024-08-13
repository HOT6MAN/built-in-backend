package com.example.hotsix.dto.notification;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponseDto {
//    private Long id;
    private Long receiverId;
    private Long senderId;
    private String response;
    private String type;
    private String notifyDate;
}
