package com.laboratory.paper.domain.paper;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PaperListItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 4092180533815351184L;

    private Long id;
    private String value;
}
