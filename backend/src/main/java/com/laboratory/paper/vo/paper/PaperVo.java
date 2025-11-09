package com.laboratory.paper.vo.paper;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class PaperVo implements Serializable {
    @Serial
    private static final long serialVersionUID = -6098943723700758822L;

    private Long id;
    private String title;
    private String author;
    private LocalDate publishTime;
    private String journal;
    private String journalType;
    private String researchDirection;
    private String innovation;
    private String deficiency;
    private String keywords;
}