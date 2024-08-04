package com.example.hotsix.model;

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
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;
    @Column(name = "is_used")
    private Boolean isUsed;
}
