package com.iflytek.aiui.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static String getDateStr() {
        return sdf.format(new Date());
    }
}
