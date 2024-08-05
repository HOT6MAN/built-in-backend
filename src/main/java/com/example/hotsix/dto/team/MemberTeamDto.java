package com.example.hotsix.dto.team;

import com.example.hotsix.dto.member.MemberDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberTeamDto {

    private Long id;
    private MemberDto member;
    private boolean leader;


    @Override
    public String toString() {
        return "MemberTeamDto{" +
                "id=" + id +
                ", member=" + member +
                //", team=" + team +
                ", leader=" + leader +
                '}';
    }
}
