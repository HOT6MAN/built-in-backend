package com.example.hotsix.repository.recruit;

import com.example.hotsix.model.Recruit;
import com.example.hotsix.repository.Querydsl4RepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.hotsix.model.QRecruit.recruit;

@Repository
public class RecruitRepositoryImpl extends Querydsl4RepositorySupport implements RecruitRepositoryCustom {

    @Override
    public List<Recruit> findRecruitsByTeamName(String teamName) {
        return selectFrom(recruit)
                .where(recruit.team.name.startsWith(teamName))
                .fetch();
    }

    @Override
    public List<Recruit> findRecruitsByAuthorName(String authorName) {
        return selectFrom(recruit)
                .where(recruit.author.name.startsWith(authorName))
                .fetch();
    }
}
