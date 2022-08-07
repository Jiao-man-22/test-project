package com.jiao.testproject.testproject.utils;

import com.jiao.testproject.testproject.dto.UserDto;

public class SessionLocal {

    private static ThreadLocal<UserDto> local = new ThreadLocal<UserDto>();
    /**
     * 设置用户信息
     * @param user
     */

    public static void setUser( UserDto user )
    {
        local.set( user );
    }

    public static UserDto getUser(){
        System.out.println( "当前线程：" + Thread.currentThread().getName() );
        return local.get();
    }

    public String load() {
        UserDto user = SessionLocal.getUser();
        if(user != null)
        {
            System.out.println("当前用户："+user.getUserName());
        } else {
            System.out.println("用户为空");
        }
        return "exist";
    }





}












