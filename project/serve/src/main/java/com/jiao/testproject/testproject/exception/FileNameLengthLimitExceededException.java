package com.jiao.testproject.testproject.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//配置成全局异常处理类了
@RestControllerAdvice
public class FileNameLengthLimitExceededException extends Throwable {

    Logger log = LoggerFactory.getLogger(FileNameLengthLimitExceededException.class);


    //在方法上加上@ExceptionHandler注解并指定想处理的异常类型，接着在方法内编写对该异常的操作逻辑，就完成了对该异常的全局处理
    @ExceptionHandler({CustomException.class})
    public ResultBean<?> processCustomException(CustomException e) {
        log.error(e.getMessage(), e);
        return new ResultBean<String>(ResultBean.UNKNOWN_EXCEPTION, false, e.getMessage());
    }



}
