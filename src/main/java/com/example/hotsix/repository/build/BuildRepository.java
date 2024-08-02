package com.example.hotsix.repository.build;

import com.example.hotsix.model.MemberProjectCredential;
import com.example.hotsix.model.MemberProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildRepository extends JpaRepository<MemberProjectInfo, Long>, BuildRepositoryCustom {
}
