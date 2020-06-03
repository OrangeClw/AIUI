package com.iflytek.aiui.service;

import com.iflytek.aiui.pojo.User;

import java.util.List;

public interface UserService {
    public List<User> list();
    public List<User> voiceSearch(String condition);
    public User query(User user);
    public User login(User user);
    public int insert(User user);
    public int update(User user);
    public int delete(User user);
}
