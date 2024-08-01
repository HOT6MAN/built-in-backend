package com.example.hotsix.model;

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

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "leader")
    private boolean leader;

}
