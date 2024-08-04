package com.example.hotsix.repository.build;

import com.example.hotsix.model.project.TeamProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildRepository extends JpaRepository<TeamProjectInfo, Long>, BuildRepositoryCustom {
}
