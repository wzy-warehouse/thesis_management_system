package com.laboratory.paper.domain.folder;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class QueryFolderResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 3116422798063635903L;

    private Long id;
    private String label;
    private Boolean hasChildren;
}
