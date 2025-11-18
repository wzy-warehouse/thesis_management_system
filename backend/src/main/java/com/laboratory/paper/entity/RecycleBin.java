package com.laboratory.paper.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecycleBin {

  private Long id;
  private Long paperId;
  private Long parentId;
  private LocalDateTime deleteTime;
  private Long deleteUser;

}
