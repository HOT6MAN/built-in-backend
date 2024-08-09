package com.example.hotsix.controller.resume;

import com.example.hotsix.dto.apply.ApplyRequest;
import com.example.hotsix.dto.resume.ResumeRequest;
import com.example.hotsix.dto.resume.ResumeResponse;
import com.example.hotsix.dto.resume.ResumeShortResponse;
import com.example.hotsix.editor.ExperienceRequestPropertyEditor;
import com.example.hotsix.editor.ResumePropertyEditor;
import com.example.hotsix.model.*;
import com.example.hotsix.oauth.dto.CustomOAuth2User;
import com.example.hotsix.service.member.MemberService;
import com.example.hotsix.service.resume.ResumeService;
import com.example.hotsix.service.storage.StorageService;
import com.example.hotsix.service.team.TeamService;
import jakarta.inject.Provider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.example.hotsix.model.QTeam.team;

@RestController
public class ResumeController {

    private final MemberService memberService;
    private final TeamService teamService;
    private final ResumeService resumeService;
    private final StorageService storageService;
    private final ExperienceRequestPropertyEditor experienceRequestPropertyEditor;
    private final Provider<ResumePropertyEditor> resumePropertyEditorProvider;

    public ResumeController(MemberService memberService,
                            TeamService teamService,
                            ResumeService resumeService,
                            @Qualifier("fileSystemStorageService") StorageService storageService,
                            ExperienceRequestPropertyEditor experienceRequestPropertyEditor,
                            Provider<ResumePropertyEditor> resumePropertyEditorProvider
    ) {
        this.memberService = memberService;
        this.teamService = teamService;
        this.resumeService = resumeService;
        this.storageService = storageService;
        this.experienceRequestPropertyEditor = experienceRequestPropertyEditor;
        this.resumePropertyEditorProvider = resumePropertyEditorProvider;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(List.class, "experiences", experienceRequestPropertyEditor);
        binder.registerCustomEditor(Resume.class, resumePropertyEditorProvider.get());
    }

    @GetMapping("/resume/{id}")
    public ResumeResponse preview(@PathVariable("id") Resume resume) {
        return resume.toResponse(storageService.getUploadedImageUrl(resume.getProfile()));
    }

    @GetMapping("/resumes")
    public List<ResumeShortResponse> myResumes(@AuthenticationPrincipal CustomOAuth2User me) throws InterruptedException {
        Thread.sleep(1000*3);

        Member memberMe = memberService.findById(me.getId());

        return Objects.nonNull(memberMe.getResumeList()) ? convertToShortResponse(memberMe.getResumeList()) : null;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/resume")
    public void registerResume(@ModelAttribute ResumeRequest resumeRequest, @AuthenticationPrincipal CustomOAuth2User me) throws IOException {
        Member author = memberService.findById(me.getId());

        Resume newResume = resumeRequest.toEntity(author);
        resumeRequest.experiences().forEach(newResume::addExperience);

        storageService.store(resumeRequest.profile());
        resumeService.save(newResume);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/resume/{id}")
    public void updateRecruit(@PathVariable("id") Resume resume, @ModelAttribute ResumeRequest resumeRequest) throws IOException {
        String storedProfileName = resume.getProfile();
        MultipartFile profileToUpload = resumeRequest.profile();

        if (storedProfileName == null && profileToUpload != null) {
            storageService.store(profileToUpload);
        } else if (storedProfileName != null && profileToUpload == null) {
            storageService.remove(storedProfileName);
        } else if (storedProfileName != null) {
            storageService.replace(storedProfileName, profileToUpload);
        }

        resume.update(resumeRequest);
        resumeService.save(resume);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/resume/{id}")
    public void deleteRecruit(@PathVariable("id") Resume resume) throws IOException {
        storageService.remove(resume.getProfile());

        resumeService.delete(resume);
    }

    @PostMapping("/apply")
    @ResponseStatus(HttpStatus.CREATED)
    public void apply(@RequestBody ApplyRequest applyRequest) {
        Long resumeId = applyRequest.resumeId();
        Long teamId = applyRequest.teamId();

        Resume application = resumeService.findById(resumeId);
        Team teamToApply = teamService.findById(teamId);

        resumeService.apply(teamToApply, application);
    }

    private List<ResumeShortResponse> convertToShortResponse(List<Resume> list) {
        return list.stream().map(resume -> resume.toShortResponse(storageService.getUploadedImageUrl(resume.getProfile()))).toList();
    }
}
