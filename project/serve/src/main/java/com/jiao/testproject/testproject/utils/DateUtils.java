package com.jiao.testproject.testproject.utils;

import javafx.util.converter.LocalDateTimeStringConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        // 指定格式化格式
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
        return f.format(now);
    }

    public static String getSysTime(){
        long currentTimeMillis=System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        f.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return f.format(now);

    }

    public static String getSysTimeDoId(){
        Date now = new Date();
        SimpleDateFormat f = new SimpleDateFormat("h-mm-ss");
        return f.format(now);
    }

    //格式化时间
    public static String formatTime(Long timeParma) {

        //LocalDateTime-->>字符串
/*        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateString = dateTimeFormatter2.format(LocalDateTime.now());
        System.out.println("日期转字符串: " + dateString);*/

/*        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(String.valueOf(timeParma), dateTimeFormatter);
        System.out.println("字符串转LocalDateTime: " + localDateTime);*/
        //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Date now = new Date(timeParma);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd:HH:MM");
        return f.format(now);
    }

}
