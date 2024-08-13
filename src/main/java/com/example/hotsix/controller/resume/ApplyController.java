package com.example.hotsix.controller.resume;

import com.example.hotsix.dto.apply.ApplyRequest;
import com.example.hotsix.dto.apply.ApplyShortResponse;
import com.example.hotsix.dto.resume.ApproveRequest;
import com.example.hotsix.editor.TeamPropertyEditor;
import com.example.hotsix.model.Apply;
import com.example.hotsix.model.Resume;
import com.example.hotsix.model.Team;
import com.example.hotsix.model.id.ApplyId;
import com.example.hotsix.service.resume.ApplyService;
import com.example.hotsix.service.resume.ResumeService;
import com.example.hotsix.service.team.TeamService;
import jakarta.inject.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApplyController {

    private final TeamService teamService;
    private final ResumeService resumeService;
    private final ApplyService applyService;
    private final Provider<TeamPropertyEditor> teamPropertyEditorProvider;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Team.class, teamPropertyEditorProvider.get());
    }

    @PostMapping("/apply")
    @ResponseStatus(HttpStatus.CREATED)
    public void apply(@RequestBody ApplyRequest applyRequest) {
        Long resumeId = applyRequest.resumeId();
        Long teamId = applyRequest.teamId();

        Resume application = resumeService.findById(resumeId);
        Team teamToApply = teamService.findById(teamId);

        applyService.apply(teamToApply, application);
    }

    @GetMapping("/applications/team/{teamId}")
    public List<ApplyShortResponse> teamApplications(@PathVariable("teamId") Team team) {
        return team.getApplications().stream().map(Apply::toShortResponse).toList();
    }

    @PatchMapping("/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approve(@RequestBody ApproveRequest approveRequest) {
        ApplyId applyId = new ApplyId(approveRequest.teamId(), approveRequest.resumeId());

        applyService.approve(applyId);
    }

    @PatchMapping("/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(@RequestBody ApproveRequest approveRequest) {
        ApplyId applyId = new ApplyId(approveRequest.teamId(), approveRequest.resumeId());

        applyService.reject(applyId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/apply/team/{teamId}/application/{resumeId}")
    public void delete(@PathVariable("teamId") Long teamId, @PathVariable("resumeId") Long resumeId) {
        ApplyId applyId = new ApplyId(teamId, resumeId);

        applyService.delete(applyId);
    }
}
