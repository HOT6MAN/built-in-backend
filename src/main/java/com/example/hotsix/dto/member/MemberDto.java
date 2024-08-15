package com.example.hotsix.dto.member;

import lombok.*;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String profileUrl;
    private String phone;
    private String address;
    private String role;
    private String lgnMtd;
    private LocalDateTime regDTTM;

    @Override
    public String toString() {
        return "MemberDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                ", lgnMtd='" + lgnMtd + '\'' +
                '}';
    }
}
