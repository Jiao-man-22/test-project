package configs;

import com.jiao.testproject.testproject.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.lang.annotation.Annotation;

/**
 * <pre>
 * 相当于xml 配置类 配置拦截器 定义一个拦截爱情
 * </pre>
 */
@Configurable
public class InterceptorConfig implements WebMvcConfigurer {

    public void addInterceptors(InterceptorRegistry registry) {
        //要拦截的路径
        String[] addUrlPattern = {
                ""
        };

        //过滤的路径
        String[] excludeUrlPatter = {
                ""
        };
        //添加要注册的拦截器对象
        registry.addInterceptor(new UserInterceptor(new UserServiceImpl())).addPathPatterns(addUrlPattern).excludePathPatterns(excludeUrlPatter);
    }

}
