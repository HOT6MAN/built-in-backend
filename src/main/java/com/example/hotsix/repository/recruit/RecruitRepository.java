package com.example.hotsix.repository.recruit;

import com.example.hotsix.model.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long>, RecruitRepositoryCustom {

    @Query(value = "SELECT b.*, r.* FROM Recruit r " +
            "INNER JOIN Board b ON r.id = b.id " +
            "WHERE JSON_CONTAINS(r.desired_pos_list, :desiredPos, '$') ", nativeQuery = true)
    List<Recruit> findRecruitsByDesiredPos(@Param("desiredPos") String desiredPos);
}
