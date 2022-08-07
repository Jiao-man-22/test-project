package com.jiao.testproject.testproject.utils;

import configs.RedisReadWriteConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *<pre>
 *     自定义redis连接工厂ReadWriteLettuceConnectionFactory
 * 由于我们有多个只读库，为了实现动态切换，我们自己是实现一个工厂，方便后面操作
 *</pre>
 *
 */
@Slf4j
@Component
public class ReadWriteLettuceConnectionFactory implements DisposableBean {


    @Override
    public void destroy() throws Exception {

    }
}
