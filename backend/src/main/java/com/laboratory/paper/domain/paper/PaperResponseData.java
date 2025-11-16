package com.laboratory.paper.domain.paper;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PaperResponseData implements Serializable {
    @Serial
    private static final long serialVersionUID = 58902016788841601L;

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

    public PaperResponseData() {
    }

    public PaperResponseData(Long id, String title, String author, LocalDate publishTime, String journal, String journalType, String researchDirection, String innovation, String deficiency, String keywords, LocalDateTime createTime) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishTime = publishTime;
        this.journal = journal;
        this.journalType = journalType;
        this.researchDirection = researchDirection;
        this.innovation = innovation;
        this.deficiency = deficiency;
        this.keywords = keywords;
        this.createTime = createTime;
    }
}