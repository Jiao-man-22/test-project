package com.jiao.testproject.testproject.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @ClassName CorsFilter
 * @Description TODO
 * @AUTHOR jiaorongjin
 * @Date 2022/8/4 13:41
 * @Version 1.0
 **/

@Configuration
public class CorsFilterConfig {
    @Bean
    public FilterRegistrationBean<CorsFilter>
    filterFilterRegistrationBean(){
        UrlBasedCorsConfigurationSource configSource =
                new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration =
                new CorsConfiguration();

        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);

        configSource.registerCorsConfiguration("/**",
                corsConfiguration);
        FilterRegistrationBean<CorsFilter> fBean =
                new FilterRegistrationBean<>(
                        new CorsFilter(configSource));

        fBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return fBean;
    }
}
