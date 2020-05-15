package com.massky.greenlandvland.View.DateChangeWheelView.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jzxiang on 16/4/20.
 */
public class PickerContants {
    public static final String FORMAT = "%02d";
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
    static Date curDate = new Date(System.currentTimeMillis());//获取当前时间
    static String str = formatter.format(curDate);

    public static final int DEFAULT_MIN_YEAR = Integer.parseInt(str)-80;
    public static final int YEAR_COUNT = 80;
    public static final int MIN_MONTH = 1;
    public static final int MAX_MONTH = 12;
    public static final int MIN_DAY = MIN_MONTH;
    public static final int MIN_HOUR = 0;
    public static final int MAX_HOUR = 23;
    public static final int MIN_MINUTE = 0;
    public static final int MAX_MINUTE = 59;
}
