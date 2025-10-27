package com.laboratory.paper.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Paper implements Serializable {
    @Serial
    private static final long serialVersionUID = 8736487586137758624L;

    private Long id;
    private String title;
    private String author;
}
