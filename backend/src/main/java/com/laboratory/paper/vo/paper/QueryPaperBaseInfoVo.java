package com.laboratory.paper.vo.paper;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class QueryPaperBaseInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = -1572512230123352452L;

    @NotNull(message = "文件夹id不能为空")
    @Min(value = 0, message = "文件夹id不能小于0")
    private Long folderId;

    @NotNull(message = "论文id不能为空")
    @Min(value = 0, message = "论文id不能小于0")
    private Long paperId;

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum;

    @NotNull(message = "分页大小不能为空")
    @Min(value = 1, message = "分页大小不能小于1")
    private Integer pageSize;
}
