package filter;


import com.jiao.testproject.testproject.dto.UserDto;
import com.jiao.testproject.testproject.entity.UserEntity;
import com.jiao.testproject.testproject.services.impl.FileServiceImpl;
import com.jiao.testproject.testproject.services.impl.UserServiceImpl;
import com.jiao.testproject.testproject.utils.SessionLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

@Order(1)
@WebFilter(filterName = "SessionFilter", urlPatterns = "/*" , initParams = {@WebInitParam(name = "URL", value = "http://192.168.1.77:8082")})
public class SessionFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SessionFilter.class);

    @Autowired
    UserServiceImpl userServiceImpl;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        UserEntity userinfo = userServiceImpl.getUserMap().get("userinfo");
        if(userinfo == null){
            log.info("用户信息为null 需要重新登录");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
