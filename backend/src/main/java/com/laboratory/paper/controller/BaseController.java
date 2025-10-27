package com.laboratory.paper.controller;

import com.laboratory.paper.entity.ApiResponse;
import com.laboratory.paper.service.ex.ServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 基础controller类，所有controller层对象必须继承这个类
 */
public class BaseController{

    // 用于统一处理抛出的异常
    @ExceptionHandler(ServiceException.class)
    public ApiResponse<Void> handleException(Throwable e) {
        return ApiResponse.error(e.getMessage());
    }
}