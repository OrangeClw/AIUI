package com.iflytek.aiui.utils;

import java.io.*;

public class UsernameUtils {
    private static Integer BEGIN;
    private static Integer STEP = 1;

    public static String generator() throws IOException {
        return "test" + generatorNum();
    }

    public static Integer generatorNum() throws IOException {
        InputStream is = new FileInputStream("D:\\JavaEE\\intellsoft\\aiui\\src\\main\\java\\com\\iflytek\\aiui\\utils\\generatorNum.txt");
        BEGIN = is.read();
        BEGIN = BEGIN++ + 1;
        OutputStream os = new FileOutputStream("D:\\JavaEE\\intellsoft\\aiui\\src\\main\\java\\com\\iflytek\\aiui\\utils\\generatorNum.txt");
        os.write(BEGIN);
        is.close();
        os.close();
        return BEGIN;
    }

    public static String generatorLogin() throws IOException {
        return "upload" + generatorNum();
    }
}