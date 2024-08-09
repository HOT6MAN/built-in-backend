package com.example.hotsix.model;

import com.example.hotsix.enums.ApplicationStatus;
import com.example.hotsix.model.id.ApplyId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "apply")
@SuperBuilder
@NoArgsConstructor
@Getter
public class Apply extends BaseEntity {

    @EmbeddedId
    private ApplyId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status;

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @MapsId("resumeId")
    @JoinColumn(name = "resume_id")
    private Resume resume;
}
