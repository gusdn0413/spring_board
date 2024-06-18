package com.nc13.spring_board.service;

import com.nc13.spring_board.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// db 통신 담당
@Service
@RequiredArgsConstructor
public class UserService {

    private final SqlSession sqlSession;
    private final String NAMESPACE = "com.nc13.mappers.UserMapper.";

    public UserDTO auth(UserDTO userDTO) {
        return sqlSession.selectOne(NAMESPACE + "auth", userDTO);
    }

    public boolean validateUsername(UserDTO userDTO) {
        return sqlSession.selectOne(NAMESPACE + "selectByUsername", userDTO) == null;
    }

    public void register(UserDTO userDTO) {
        sqlSession.insert(NAMESPACE + "register", userDTO);
    }
}
