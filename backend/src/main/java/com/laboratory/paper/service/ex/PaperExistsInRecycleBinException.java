package com.laboratory.paper.service.ex;

public class PaperExistsInRecycleBinException extends ServiceException{
    public PaperExistsInRecycleBinException() {
        super("回收站中已经存在该论文，请勿重复删除。");
    }

    public PaperExistsInRecycleBinException(String message) {
        super(message);
    }
}
