package com.jiao.testproject.testproject.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

@Component
@Aspect
public class CustomerAnnotationParse {

    //会切入这个方法
    @Pointcut("@annotation(CustomerAnnotation)")
    public void pointcut(){}


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 参数值
        Object[] args = pjp.getArgs();
        System.out.println(pjp.getSignature().getName());
        System.out.println(pjp.getSignature().getDeclaringType().getSimpleName());
        Object target = pjp.getTarget();
        Object aThis = pjp.getThis();
        System.out.println("被代理的对象:" + target);
        System.out.println(target.hashCode());
        System.out.println("代理对象自己:" + aThis);
        System.out.println(aThis.hashCode());
        System.out.println(target.equals(aThis));
        // 参数列表
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        //先得到被注解的方法 再获取其方法上的注解对象 再获得注解的值
        CustomerAnnotation annotation1 = method.getAnnotation(CustomerAnnotation.class);
        System.out.println(annotation1.name());
        System.out.println(annotation1.age());
        Arrays.stream(annotation1.score()).forEach(System.out::println);
        Parameter[] mParameters = method.getParameters();

        for (int i = 0; i < mParameters.length; i++) {
            System.out.println("获取方法参数 "+mParameters[i]);
            if (mParameters[i].isAnnotationPresent(CustomerAnnotation.class)){
                System.out.println("参数上包含注解");
                //拿到参数上的注解
                Annotation annotation = mParameters[i].getAnnotation(CustomerAnnotation.class);
                System.out.println(annotation.toString());
            }
        }
        pjp.proceed();
        return null;
    }

}
