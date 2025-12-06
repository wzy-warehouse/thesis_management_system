package com.laboratory.paper.service.ex;

public class PapersIsEmptyException extends ServiceException{
    public PapersIsEmptyException() {
        super("请至少选择一个论文进行删除");
    }
}
