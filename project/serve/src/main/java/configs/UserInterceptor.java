package configs;

import com.jiao.testproject.testproject.services.IUserService;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserInterceptor implements HandlerInterceptor {

    private IUserService userService;
    public UserInterceptor(IUserService  userServiceParam){
    userService = userServiceParam;
    }
}
