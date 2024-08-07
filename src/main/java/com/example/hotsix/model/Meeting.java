package com.example.hotsix.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

@Entity
@Table(name = "meeting")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meeting extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false, foreignKey = @ForeignKey(name = "fk_team_meeting_id_team_id"))
    private Team team;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "connection_id", length = 100)
    private String connectionId;

    @Column(name = "user_count")
    private Integer userCount;

    @Column(name = "session_status", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean sessionStatus;

}
