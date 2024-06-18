package com.nc13.spring_board.service;

import com.nc13.spring_board.model.BoardDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
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
}
