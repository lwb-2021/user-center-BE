package com.github.lwb_2021.user_center.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotLoginException.class)
    public SaResult handleNotLoginException(NotLoginException e) {
        SpringMVCUtil.getResponse().setStatus(401);
        return SaResult.get(401, e.getMessage(), null);
    }

}
