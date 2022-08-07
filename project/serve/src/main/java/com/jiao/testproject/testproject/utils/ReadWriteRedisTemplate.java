package com.jiao.testproject.testproject.utils;

import org.springframework.data.redis.core.RedisTemplate;

public class ReadWriteRedisTemplate<K,V> extends RedisTemplate {
    public void setReadWriteConnectionFactory(ReadWriteLettuceConnectionFactory factory, boolean isRead) {
    }
}
