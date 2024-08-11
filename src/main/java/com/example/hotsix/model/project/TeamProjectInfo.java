package com.example.hotsix.model.project;

import com.example.hotsix.dto.build.TeamProjectInfoDto;
import com.example.hotsix.model.ServiceSchedule;
import com.example.hotsix.model.Team;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "team_project_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamProjectInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;

    @JsonBackReference
    @OneToOne(mappedBy = "teamProjectInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private ServiceSchedule serviceSchedule;

    @JsonManagedReference
    @OneToMany(mappedBy = "projectInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BackendConfig> backendConfigs = new ArrayList<>();
    @JsonManagedReference
    @OneToMany(mappedBy = "projectInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FrontendConfig> frontendConfigs = new ArrayList<>();
    @JsonManagedReference
    @OneToMany(mappedBy = "projectInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DatabaseConfig> databaseConfigs = new ArrayList<>();

    public TeamProjectInfoDto toDto(){
        return TeamProjectInfoDto.builder()
                .id(id)
                .title(title)
                .backendConfigs(backendConfigs.stream().map(BackendConfig::toDto).collect(Collectors.toList()))
                .frontendConfigs(frontendConfigs.stream().map(FrontendConfig::toDto).collect(Collectors.toList()))
                .databaseConfigs(databaseConfigs.stream().map(DatabaseConfig::toDto).collect(Collectors.toList()))
                .build();
    }

    // 양방향 관계 편의 메서드
    public void setTeam(Team team) {
        // 기존 팀과 관계를 제거
        if (this.team != null) {
            this.team.getTeamProjectInfos().remove(this);
        }

        this.team = team;
        team.getTeamProjectInfos().add(this);
    }

    public void setPropertiesFromDto(TeamProjectInfoDto dto){
        this.id = dto.getId();
        this.backendConfigs = dto.getBackendConfigs().stream().map(BackendConfig::fromDto).collect(Collectors.toList());
        this.frontendConfigs = dto.getFrontendConfigs().stream().map(FrontendConfig::fromDto).collect(Collectors.toList());
        this.databaseConfigs = dto.getDatabaseConfigs().stream().map(DatabaseConfig::fromDto).collect(Collectors.toList());
    }
}
