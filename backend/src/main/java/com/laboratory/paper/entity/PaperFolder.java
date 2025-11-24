package com.laboratory.paper.entity;

import lombok.Data;

@Data
public class PaperFolder {

  private Long id;
  private Long paperId;
  private Long folderId;

  public PaperFolder() {
  }

  public PaperFolder(Long paperId, Long folderId) {
    this.paperId = paperId;
    this.folderId = folderId;
  }
}
