package com.iflytek.aiui.service.impl;

import com.iflytek.aiui.mapper.UserMapper;
import com.iflytek.aiui.pojo.User;
import com.iflytek.aiui.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> list() {
        return userMapper.list();
    }

    @Override
    public List<User> voiceSearch(String condition) {
        return userMapper.voiceSearch(condition);
    }

    @Override
    public User query(User user) {
        return userMapper.query(user);

    }

    @Override
    public User login(User user) {
        return userMapper.query(user);
    }

    @Override
    public int insert(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int update(User user) {
        return userMapper.update(user);
    }

    @Override
    public int delete(User user) {
        return userMapper.delete(user);
    }
}
