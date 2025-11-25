package com.laboratory.paper.controller;

import com.laboratory.paper.domain.ApiResponse;
import com.laboratory.paper.service.ex.FilesInRecycleBinException;
import com.laboratory.paper.service.ex.NoLoginException;
import com.laboratory.paper.service.ex.ServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 基础controller类，所有controller层对象必须继承这个类
 */
public class BaseController{

    // 用于统一处理应用层抛出的异常
    @ExceptionHandler(ServiceException.class)
    public ApiResponse<Void> handleServiceException(Throwable e) {
        if(e instanceof FilesInRecycleBinException) {
            return ApiResponse.error(409, e.getMessage());
        }else if(e instanceof NoLoginException) {
            return ApiResponse.error(401, e.getMessage());
        }else {
            return ApiResponse.error(e.getMessage());
        }
    }

    // 用于统一处理其余抛出的异常
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Void> handleException(Throwable e) {
        return ApiResponse.error(e.getMessage());
    }
}