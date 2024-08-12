package com.example.hotsix.repository.apply;

import com.example.hotsix.model.Apply;
import com.example.hotsix.model.id.ApplyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ApplyRepository extends JpaRepository<Apply, ApplyId>, ApplyRepositoryCustom  {
}
