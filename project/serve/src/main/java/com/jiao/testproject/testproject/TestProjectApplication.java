package com.jiao.testproject.testproject;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import java.util.concurrent.CountDownLatch;

@EnableTransactionManagement
@SpringBootApplication
@EnableConfigurationProperties
@EnableDubbo(scanBasePackages = {"com.jiao.testproject.testproject.services"})
public class TestProjectApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(TestProjectApplication.class, args);
        System.out.println("dubbo service started");
        new CountDownLatch(1).await();
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                connector.setProperty("relaxedQueryChars", "|{}[]");
            }
        });
        return factory;
    }
    //JPAQueryFactory 加入 spring 容器
    @Bean
    public JPAQueryFactory jpaQueryFactory(@Autowired EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }




}
