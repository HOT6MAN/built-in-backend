package com.example.hotsix.dto.team;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDto {

    private String name;
    private String content;
    private Long memberId;

    @Override
    public String toString() {
        return "TeamDto{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", memberId='" + memberId + '\'' +
                '}';
    }
}
