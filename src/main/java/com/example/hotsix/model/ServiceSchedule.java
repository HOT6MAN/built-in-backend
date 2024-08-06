package com.example.hotsix.model;

import com.example.hotsix.model.project.TeamProjectInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "service_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;
    @OneToOne
    @JoinColumn(name = "team_project_info_id", referencedColumnName = "id")
    private TeamProjectInfo teamProjectInfo;

    @Column(name = "is_used")
    private Boolean isUsed;
    @Column(name = "is_pendding")
    private Boolean isPendding;

    @Override
    public String toString() {
        return "ServiceSchedule{" +
                "id=" + id +
                ", isUsed=" + isUsed +
                ", isPendding=" + isPendding +
                '}';
    }
}
