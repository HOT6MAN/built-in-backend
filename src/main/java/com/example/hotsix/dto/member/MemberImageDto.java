package com.example.hotsix.dto.member;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberImageDto {
    private Long id;
    private String originName;
    private String fixedName;
    private String saveFolder;
    private String type;
}
