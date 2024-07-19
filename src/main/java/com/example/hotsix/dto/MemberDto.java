package com.example.hotsix.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class MemberDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileUrl;
    private String phone;
    private String address;
    private boolean deleted;
}