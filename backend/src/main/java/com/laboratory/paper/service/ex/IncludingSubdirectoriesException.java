package com.laboratory.paper.service.ex;

public class IncludingSubdirectoriesException extends ServiceException{
    public IncludingSubdirectoriesException() {
        super("当前目录存在子目录，请先删除子目录后再删除该目录。");
    }
}
