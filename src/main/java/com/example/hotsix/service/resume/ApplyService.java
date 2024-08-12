package com.example.hotsix.service.resume;

import com.example.hotsix.model.Apply;
import com.example.hotsix.model.Resume;
import com.example.hotsix.model.Team;
import com.example.hotsix.model.id.ApplyId;
import com.example.hotsix.repository.apply.ApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final ApplyRepository applyRepository;

    public void apply(Team team, Resume resume) {
        Apply application = resume.toApplication(team);

        applyRepository.save(application);
    }

    public void approve(ApplyId applyId) {
        applyRepository.approve(applyId);
    }

    public void reject(ApplyId applyId) {
        applyRepository.reject(applyId);
    }

    public void delete(ApplyId applyId) {
        applyRepository.delete(applyId);
    }
}
