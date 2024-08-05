package com.example.hotsix.model;

import com.example.hotsix.dto.member.MemberDto;
import com.example.hotsix.dto.team.MemberTeamDto;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@Table(name="member_team")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberTeam extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    //@JoinColumn(name = "id")
    private Member member;

    @ManyToOne
    //@JoinColumn(name = "id")
    private Team team;

    @Column(name = "leader")
    private boolean leader;

    public MemberTeamDto toDto() {
        MemberDto dto = member.toDtoForMemberTeam();

        return MemberTeamDto.builder()
                .member(dto)
                .leader(leader)
                .build();
    }


    @Override
    public String toString() {
        return "MemberTeam{" +
                ", member=" + member +
                ", leader=" + leader +
                '}';
    }
}
