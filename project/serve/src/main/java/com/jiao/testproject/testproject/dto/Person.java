package com.jiao.testproject.testproject.dto;

import lombok.Data;

@Data
public class Person {
    private String name;  // 姓名
    private int salary; // 薪资
    private int age; // 年龄
    private String sex; //性别
    private String area;  // 地区


    public Person(String name, int salary, String male, String area) {
        this.name = name;
        this.salary = salary;
        this.sex = male;
        this.area = area;
    }
}
