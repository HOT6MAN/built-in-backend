package com.example.hotsix.model;

import com.example.hotsix.dto.resume.ExperienceRespone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resume_experience")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    public static ExperienceRespone toResponse(ResumeExperience experience) {
        return new ExperienceRespone(experience.title, experience.description);
    }
}
