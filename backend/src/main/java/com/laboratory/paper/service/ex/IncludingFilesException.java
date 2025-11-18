package com.laboratory.paper.service.ex;

public class IncludingFilesException extends ServiceException{
    public IncludingFilesException() {
        super("当前目录下存在文件，请先删除文件后再删除该目录。");
    }
}
