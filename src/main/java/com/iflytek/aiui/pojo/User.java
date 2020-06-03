package com.iflytek.aiui.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class User {
    private Integer id;
    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "[A-Za-z0-9]{4,20}", message = "用户名必须为4位以上的字母或数 字")
    private String username;
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "[A-Za-z0-9]{6,20}", message = "密码必须为6位以上的字母或数字")
    private String password;
    private String nickname;
    private String sex;
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
