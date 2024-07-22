package com.example.hotsix.repository.test;

import com.example.hotsix.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Member, Long>, TestRepositoryCustom {
    Member findById(long id);
    Member findByEmail(String email);
}
