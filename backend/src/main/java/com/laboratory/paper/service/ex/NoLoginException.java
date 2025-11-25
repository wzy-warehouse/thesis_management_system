package com.laboratory.paper.service.ex;

public class NoLoginException extends ServiceException{
    public NoLoginException() {
        super("请先进行登录。");
    }
}
