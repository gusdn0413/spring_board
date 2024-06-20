package com.nc13.spring_board.service;

import com.nc13.spring_board.model.ReplyDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final String NAMESPACE = "com.nc13.mappers.ReplyMapper.";
    private final SqlSession session;

    public void insert(ReplyDTO replyDTO) {
        session.insert(NAMESPACE + "insert", replyDTO);
    }

    public List<ReplyDTO> selectAll(int boardId) {
        return session.selectList(NAMESPACE + "selectAll", boardId);
    }

    public ReplyDTO selectOne(int replyId) {
        return session.selectOne(NAMESPACE + "selectOne", replyId);
    }

    public void update(ReplyDTO replyDTO) {
        session.update(NAMESPACE + "update", replyDTO);
    }

    public void delete(int id) {
        session.delete(NAMESPACE + "delete", id);
    }
}
