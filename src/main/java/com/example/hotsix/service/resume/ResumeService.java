package com.example.hotsix.service.resume;

import com.example.hotsix.enums.Process;
import com.example.hotsix.exception.BuiltInException;
import com.example.hotsix.model.Resume;
import com.example.hotsix.repository.resume.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public Resume findById(long id) {
        return resumeRepository.findById(id).orElseThrow(() -> new BuiltInException(Process.RESUME_NOT_FOUND));
    }

    public void save(Resume newResume) {
        resumeRepository.save(newResume);
    }

    public void delete(Resume resume) {
        resumeRepository.delete(resume);
    }
}
