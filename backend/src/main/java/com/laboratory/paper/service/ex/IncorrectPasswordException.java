package com.laboratory.paper.service.ex;

public class IncorrectPasswordException extends ServiceException{
    public IncorrectPasswordException() {
        super("密码错误。");
    }
}
