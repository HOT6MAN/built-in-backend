package com.example.hotsix.model;

import com.example.hotsix.converter.ListToJsonConverter;
import com.example.hotsix.dto.resume.ExperienceRequest;
import com.example.hotsix.dto.resume.ResumeRequest;
import com.example.hotsix.dto.resume.ResumeResponse;
import com.example.hotsix.dto.resume.ResumeShortResponse;
import com.example.hotsix.enums.ApplicationStatus;
import com.example.hotsix.model.id.ApplyId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resume")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resume extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "profile", nullable = false)
    private String profile;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "tech_stack", nullable = false)
    @Convert(converter = ListToJsonConverter.class)
    private List<String> techStack;

    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeExperience> experiences = new ArrayList<>();

    public void addExperience(ExperienceRequest experienceRequest) {
        this.experiences.add(experienceRequest.toEntity(this));
    }

    public ResumeShortResponse toShortResponse(String profileUrl) {
        return new ResumeShortResponse(this.id, this.title, getModifiedDate().toLocalDate(), profileUrl);
    }

    public ResumeResponse toResponse(String profileUrl) {
        return new ResumeResponse(
            this.title,
            profileUrl,
            this.position,
            this.techStack,
            this.experiences.stream().map(ResumeExperience::toResponse).toList(),
            this.comment
        );
    }

    public void update(ResumeRequest resumeRequest) {
        this.title = resumeRequest.title();
        this.profile = resumeRequest.profile().getOriginalFilename();
        this.position = resumeRequest.position();
        this.techStack = resumeRequest.techStack();
        this.experiences.clear();
        resumeRequest.experiences().forEach(this::addExperience);
        this.comment = resumeRequest.comment();
    }

    public Apply toApplication(Team team) {
        return Apply.builder()
                .id(new ApplyId(team.getId(), this.getId()))
                .status(ApplicationStatus.applied)
                .team(team)
                .resume(this)
                .build();
    }

    public boolean isDefaultProfile() {
        return this.profile.startsWith("default");
    }
}
