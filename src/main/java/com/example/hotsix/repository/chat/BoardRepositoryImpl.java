package com.example.hotsix.repository.chat;

import com.example.hotsix.model.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.hotsix.model.QBoard.board;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public Board findBoardByBoardId(Long boardId) {
        return queryFactory.selectFrom(board)
                .where(board.id.eq(boardId))
                .fetchOne();
    }
}
