package com.jiao.testproject.testproject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Constraint(validatedBy = CustomerAnnotationParse.class)  // 关联解析类
@Target(ElementType.METHOD)  // 注解作用于方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomerAnnotation {
    String name();
    int age() default 18;
    int[] score();

}
