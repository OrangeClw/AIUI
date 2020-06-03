package com.iflytek.aiui.mapper;

import com.iflytek.aiui.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMapper {
    public List<User> list();

    public List<User> voiceSearch(String condition);

    public User query(User user);

    public int insert(User user);

    public int update(User user);

    public int delete(User user);
}

