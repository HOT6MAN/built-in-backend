package com.example.hotsix.repository.chat;

import com.example.hotsix.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
}
