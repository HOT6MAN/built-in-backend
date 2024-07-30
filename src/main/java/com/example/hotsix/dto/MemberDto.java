package com.example.hotsix.dto;

import com.example.hotsix.model.Member;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .build();
    }

    @Override
    public String toString() {
        return "MemberDto{" +
                "id=" + id +
                '}';
    }
}