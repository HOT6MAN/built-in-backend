package com.example.hotsix.dto.team;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDto {

    private Long id;
    private String name;
    private String status;
    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String gitUrl;
    private String jiraUrl;
    private List<MemberTeamDto> memberTeams;

    private Long memberId;

    @Override
    public String toString() {
        return "TeamDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", content='" + content + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", gitUrl='" + gitUrl + '\'' +
                ", jiraUrl='" + jiraUrl + '\'' +
                ", memberTeams=" + memberTeams +
                ", memberId=" + memberId +
                '}';
    }
}
