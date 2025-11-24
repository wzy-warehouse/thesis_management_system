package com.laboratory.paper.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Paper {

  private Long id;
  private String title;
  private String author;
  private LocalDate publishTime;
  private String journal;
  private String journalType;
  private String researchDirection;
  private String innovation;
  private String deficiency;
  private String filePath;
  private Long fileSize;
  private String fileHash;
  private String keywords;
  private Long createUser;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;

  public Paper() {
  }

  public Paper(Long createUser, String title, Long fileSize, String filePath, String fileHash) {
    this.createUser = createUser;
    this.title = title;
    this.fileSize = fileSize;
    this.filePath = filePath;
    this.fileHash = fileHash;
  }
}
