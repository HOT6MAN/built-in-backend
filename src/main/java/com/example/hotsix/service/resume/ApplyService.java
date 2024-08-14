package com.example.hotsix.service.resume;

import com.example.hotsix.model.Apply;
import com.example.hotsix.model.Resume;
import com.example.hotsix.model.Team;
import com.example.hotsix.model.id.ApplyId;
import com.example.hotsix.repository.apply.ApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final ApplyRepository applyRepository;

    @Transactional
    public void apply(Team team, Resume resume) {
        Apply application = resume.toApplication(team);

        applyRepository.save(application);
    }

    @Transactional
    public void approve(ApplyId applyId) {
        applyRepository.approve(applyId);
    }

    @Transactional
    public void reject(ApplyId applyId) {
        applyRepository.reject(applyId);
    }

    @Transactional
    public void delete(ApplyId applyId) {
        applyRepository.delete(applyId);
    }
}
