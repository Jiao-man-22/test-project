package com.jiao.testproject.testproject.controller;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConverterRegistry;


import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.util.CharsetUtil;
import com.mysema.commons.lang.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Date;

@RestController
public class TestHutool {
    @GetMapping("/testHutool")
    public void testHutool(){
//        int a = 3423;
//        ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
//        String result = converterRegistry.convert(String.class, a);
//        System.out.println(result);

//        String a = "2017-05-06 30:11:44";
//        Date value = Convert.toDate(a);
//        LocalDateTime currentTime = LocalDateTime.now();
//        System.out.println("当前时间: " + currentTime);
//        LocalDate date1 = currentTime.toLocalDate();
//        System.out.println("date1: " + date1);
//        Month month = currentTime.getMonth();
//        int day = currentTime.getDayOfMonth();
//        int seconds = currentTime.getSecond();
//        System.out.println("月: " + month +", 日: " + day +", 秒: " + seconds);
//        LocalDateTime date2 = currentTime.withDayOfMonth(10).withYear(2012);
//        System.out.println("date2: " + date2);
//
//        // 12 december 2014
//        LocalDate date3 = LocalDate.of(2014, Month.DECEMBER, 12);
//        System.out.println("date3: " + date3);
//
//        // 22 小时 15 分钟
//        LocalTime date4 = LocalTime.of(22, 15);
//        System.out.println("date4: " + date4);
//
//        // 解析字符串
//        LocalTime date5 = LocalTime.parse("2012-05-05 20:15:30");
//        System.out.println("date5: " + date5);

//        BufferedInputStream in = FileUtil.getInputStream("E:/a.txt");
//        BufferedOutputStream out = FileUtil.getOutputStream("E:/test2.txt");
//        BufferedReader reader = IoUtil.getReader(in,CharsetUtil.CHARSET_UTF_8);
//        OutputStreamWriter writer = IoUtil.getWriter(out, CharsetUtil.CHARSET_UTF_8);
//        long copySize = IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE);

        File file = FileUtil.file("E:/a.txt");
        String type = FileTypeUtil.getType(file);
        FileTypeUtil.putFileType("qwertyyuiopasdgg", "customerName");
        //System.out.println(type);
        //Console.log(type);
//这里只监听文件或目录的修改事件
        WatchMonitor watchMonitor = WatchMonitor.create(file, WatchMonitor.ENTRY_MODIFY);
        watchMonitor.setWatcher(new Watcher(){
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                //Console.log("创建：{}-> {}", currentPath, obj);
            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                //Console.log("修改：{}-> {}", currentPath, obj);
            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                //Console.log("删除：{}-> {}", currentPath, obj);
            }

            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                //Console.log("Overflow：{}-> {}", currentPath, obj);
            }
        });

        //设置监听目录的最大深入，目录层级大于制定层级的变更将不被监听，默认只监听当前层级目录
        watchMonitor.setMaxDepth(3);
        //启动监听
        watchMonitor.start();

        FileAppender appender = new FileAppender(file, 16, true);

        for (int i = 0; i < 16; i++) {
            appender.append("xyz");
        }
        appender.flush();
        appender.toString();


    }
}
