package com.laboratory.paper.domain.paper;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PaperResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 8736487586137758624L;

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
    private LocalDateTime createTime;
}