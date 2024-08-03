package com.example.hotsix.repository.recruit;


import com.example.hotsix.model.Recruit;

import java.util.List;

public interface RecruitRepositoryCustom {
    List<Recruit> findRecruitsByTeamName(String teamName);
    List<Recruit> findRecruitsByAuthorName(String authorName);
}
