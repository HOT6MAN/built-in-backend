package com.example.hotsix.service.resume;

import com.example.hotsix.dto.resume.ApproveRequest;
import com.example.hotsix.enums.Process;
import com.example.hotsix.exception.BuiltInException;
import com.example.hotsix.model.Apply;
import com.example.hotsix.model.Resume;
import com.example.hotsix.model.Team;
import com.example.hotsix.model.id.ApplyId;
import com.example.hotsix.repository.apply.ApplyRepository;
import com.example.hotsix.repository.resume.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    @Transactional(readOnly = true)
    public Resume findById(long id) {
        return resumeRepository.findById(id).orElseThrow(() -> new BuiltInException(Process.RESUME_NOT_FOUND));
    }

    @Transactional
    public void save(Resume newResume) {
        resumeRepository.save(newResume);
    }

    @Transactional
    public void delete(Resume resume) {
        resumeRepository.delete(resume);
    }
}
