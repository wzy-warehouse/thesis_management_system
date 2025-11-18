package com.laboratory.paper.service.ex;

public class FilesInRecycleBinException extends ServiceException{
    public FilesInRecycleBinException() {
        super("该目录还有文件存在回收站中。");
    }
}
