package com.iflytek.aiui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.iflytek.aiui.mapper")
@ServletComponentScan
public class AiuiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiuiApplication.class, args);
    }

}
