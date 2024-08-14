package com.example.hotsix.service.recruit;

import com.example.hotsix.exception.BuiltInException;
import com.example.hotsix.model.Recruit;
import com.example.hotsix.repository.recruit.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.hotsix.enums.Process.RECRUIT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;

    @Transactional(readOnly = true)
    public Recruit findById(Long id) {
        return recruitRepository.findById(id).orElseThrow(() -> new BuiltInException(RECRUIT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Recruit> findAll() {
        return recruitRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Recruit> findAllByTeamName(String teamName) {
        return recruitRepository.findRecruitsByTeamName(teamName);
    }

    @Transactional(readOnly = true)
    public List<Recruit> findAllByAuthorName(String authorName) {
        return recruitRepository.findRecruitsByAuthorName(authorName);
    }

    @Transactional(readOnly = true)
    public List<Recruit> findAllByDesiredPos(String desiredPos) {
        return recruitRepository.findRecruitsByDesiredPos("\"" + desiredPos + "\"");
    }

    @Transactional
    public void save(Recruit recruit) {
        recruitRepository.save(recruit);
    }

    @Transactional
    public void delete(Recruit recruit) {
        recruitRepository.delete(recruit);
    }
}
