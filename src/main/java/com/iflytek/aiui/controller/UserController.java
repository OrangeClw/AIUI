package com.iflytek.aiui.controller;

import com.iflytek.aiui.exception.BaseResponseCode;
import com.iflytek.aiui.face.Base64ImgUtil;
import com.iflytek.aiui.face.Face;
import com.iflytek.aiui.pojo.User;
import com.iflytek.aiui.service.UserService;
import com.iflytek.aiui.utils.DataResult;
import com.iflytek.aiui.utils.UsernameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@Controller
public class UserController {
    public static final String UPLOAD_FILE_PATH = "D:\\JavaEE\\intellsoft\\upload\\temp\\";
    public static final String FILE_PATH = "D:\\JavaEE\\intellsoft\\upload\\face\\";

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public DataResult<User> login(@RequestBody @Valid User user, BindingResult rs, HttpServletRequest req) {

        if (rs.hasErrors()) {
            ObjectError error = rs.getAllErrors().get(0);
            DataResult<User> result = DataResult.getResult(10001, error.getDefaultMessage());
            return result;
        }
        DataResult<User> result = DataResult.success();
        user = userService.login(user);
        if (user == null) {
            result.setCode(BaseResponseCode.PASSWORD_ERROR.getCode());
            result.setMsg(BaseResponseCode.PASSWORD_ERROR.getMsg());
        } else {
            req.getSession().setAttribute("user", user);
            result.setData(user);
        }
        return result;
    }

    @PostMapping("/faceLogin")
    @ResponseBody
    public DataResult<Double> faceLogin(@CookieValue(name = "token", defaultValue = "") String token, String imgpath, HttpServletRequest request, HttpSession session) throws IOException {

        String filePath = UPLOAD_FILE_PATH + UsernameUtils.generatorLogin() + ".png";
        Base64ImgUtil.GenerateImage(imgpath, filePath);

        //对比
        DataResult<Double> result = DataResult.success();
        File file = new File(FILE_PATH);
        if (token == null || "".equals(token)) {
            String[] list = file.list();
            for (String fileName : list) {
                Double rlt = Face.login(filePath, FILE_PATH + fileName);
                if (rlt >= 0.90) {
                    String username = fileName.split("\\.")[0];
                    result.setData(rlt);
                    result.setMsg("人脸鉴权成功");
                    User user = new User();
                    user.setUsername(username);
                    user = userService.login(user);
                    session.setAttribute("user", user);
                    result.setData(rlt);
                    return result;
                }
            }
        } else {
            Double rlt = Face.login(filePath, FILE_PATH + token + ".png");
            if (rlt >= 0.90) {
                result.setData(rlt);
                result.setMsg("人脸鉴权成功");
                User user = new User();
                user.setUsername(token);
                user = userService.login(user);
                session.setAttribute("user", user);
                result.setData(rlt);
                return result;
            }
        }
        result.setCode(10002);
        result.setMsg("人脸登录鉴权失败，重试或者请选择其他登录方式");
        return result;
    }

    @PostMapping("/faceRegister")
    @ResponseBody
    public DataResult<User> faceRegister(String imgpath, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        //利用 UsernameUtils.genreator()自动生成用户名
        String username = UsernameUtils.generator();
        //生成图片路径
        String filePath = FILE_PATH + username + ".png";
        //转换编码
        Base64ImgUtil.GenerateImage(imgpath, filePath);


        DataResult<User> result = DataResult.success();

        User user = new User();
        user.setUsername(username);
        user.setPassword("123456");
        user.setAge(18);
        user.setSex("女");
        user.setNickname(username);
        userService.insert(user);
        user.setPassword("");
        session.setAttribute("user", user);
        result.setData(user);
        result.setMsg("人脸注册成功！");

        //将用户名存储到cookie
        Cookie cookie = new Cookie("token", username);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        return result;
    }

    @PostMapping("/register")
    @ResponseBody
    public DataResult<User> register(@RequestBody @Valid User user, BindingResult rs, HttpServletRequest req) {
        if (rs.hasErrors()) {
            ObjectError error = rs.getAllErrors().get(0);
            DataResult<User> result = DataResult.getResult(10001, error.getDefaultMessage());
            return result;
        }
        DataResult<User> result = DataResult.success();
        int rnt = userService.insert(user);
        if (rnt != 0) {
            result.setMsg("注册成功！");
        } else {
            result.setMsg("注册失败！");
        }
        return result;
    }
}