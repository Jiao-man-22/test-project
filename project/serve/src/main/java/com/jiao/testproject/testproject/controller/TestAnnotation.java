package com.jiao.testproject.testproject.controller;

import com.jiao.testproject.testproject.annotation.CustomerAnnotation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAnnotation {

    @GetMapping("/testAnnotation")
    @CustomerAnnotation(name="jiao",score = {12,15,16})
    public void testAnnotation(){

        System.out.println("testAnnotation() 正在执行");
    }

}
