package com.nc13.spring_board.service;

import com.nc13.spring_board.model.BoardDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final SqlSession sqlSession;
    private final String NAMESPACE = "com.nc13.mappers.BoardMapper.";

    public List<BoardDTO> showAll() {
        return sqlSession.selectList(NAMESPACE + "selectAll");
    }

    public void insert(BoardDTO boardDTO) {
        sqlSession.insert(NAMESPACE + "insert", boardDTO);
    }

    public BoardDTO selectOne(int id) {
        return sqlSession.selectOne(NAMESPACE + "selectOne", id);
    }

    public void update(BoardDTO boardDTO) {
        sqlSession.update(NAMESPACE + "update", boardDTO);
    }

    public void delete(int id) {
        sqlSession.delete(NAMESPACE + "delete", id);
    }
}
