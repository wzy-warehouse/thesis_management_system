package com.laboratory.paper.domain.paper;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class PaperBatchDeleteRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 515958941683555911L;

    private List<Long> paperIds;
    private Long folderId;
}
