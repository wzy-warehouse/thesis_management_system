package com.laboratory.paper.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ShareVo implements Serializable {
    @Serial
    private static final long serialVersionUID = -8612782335912413632L;

    private Long paperId;
    private Integer permission;
    private Integer expireDays;
}
