package com.example.hotsix.repository.chat;

import com.example.hotsix.model.Board;

public interface BoardRepositoryCustom {
    Board findBoardByBoardId(Long boardId);
}
