package com.example.hotsix.repository.apply;

import com.example.hotsix.enums.ApplicationStatus;
import com.example.hotsix.model.id.ApplyId;
import com.example.hotsix.repository.Querydsl4RepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hotsix.model.QApply.apply;

@Repository
public class ApplyRepositoryImpl extends Querydsl4RepositorySupport implements ApplyRepositoryCustom {

    @Override
    public void approve(ApplyId applyId) {
        updateFrom(apply)
                .where(apply.id.eq(applyId))
                .set(apply.status, ApplicationStatus.accepted)
                .execute();
    }

    @Override
    public void reject(ApplyId applyId) {
        updateFrom(apply)
                .where(apply.id.eq(applyId))
                .set(apply.status, ApplicationStatus.rejected)
                .execute();
    }

    @Override
    public void delete(ApplyId applyId) {
        deleteFrom(apply)
                .where(apply.id.eq(applyId))
                .execute();
    }
}
