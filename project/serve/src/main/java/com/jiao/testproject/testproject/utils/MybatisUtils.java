package com.jiao.testproject.testproject.utils;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUtils {

    private static final Logger log = LoggerFactory.getLogger(MybatisUtils.class);
    public static  SqlSession getSqlSession(){
        //将配置文件读入字节流
        SqlSession session = null;
        try (InputStream is = Resources.getResourceAsStream("mybatis/mybatis-config.xml")){
            //构建sqlSession工厂
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            //获取sqlSession
            session = sqlSessionFactory.openSession();
        } catch (IOException e) {
            log.info("MybatisUtils===="+e);
        }
        return session;

    }

}
