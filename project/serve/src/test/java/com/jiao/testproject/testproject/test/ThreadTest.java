package com.jiao.testproject.testproject.test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ThreadTest {
    private static int count = 0;
    @org.junit.jupiter.api.Test
   public static void main(String[] args){
       Thread thread = new Thread(new Runnable() {
           @Override
           public void run() {
               count++;
               System.out.println(Thread.currentThread().getName() + "\t" + Thread.currentThread().getId() + count);
           }
       });
   }

    Thread thread2 = new Thread(new Runnable() {
        @Override
        public void run() {
            count ++;
            System.out.println(Thread.currentThread().getName() + "\t"+ Thread.currentThread().getId() + count);
        }
    });

}