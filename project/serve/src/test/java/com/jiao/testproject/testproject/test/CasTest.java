package com.jiao.testproject.testproject.test;

import java.util.concurrent.atomic.AtomicInteger;

public class CasTest {


    private static final int THREADS_COUNT = 20;

    static AtomicInteger race=new AtomicInteger(0);
    public static void increase() {
        race.getAndIncrement();
        System.out.println(Thread.currentThread().getName()+"   race ："+race);

    }


    public static void testCas(){
        Thread[] threads = new Thread[THREADS_COUNT];

        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 20; i++) {
                        increase();
                    }
                }
            });
            threads[i].start();
        }

        while (Thread.activeCount() > 1) {
            Thread.yield();
        }
        System.out.println(race);
    }
}
