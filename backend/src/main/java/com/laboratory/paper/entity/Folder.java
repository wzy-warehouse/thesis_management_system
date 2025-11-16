package com.laboratory.paper.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Folder {
  private Long id;
  private String name;
  private Long parentId;
  private Long createUser;
  private LocalDateTime createTime;
}
